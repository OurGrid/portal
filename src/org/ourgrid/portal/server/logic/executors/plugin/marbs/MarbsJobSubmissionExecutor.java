package org.ourgrid.portal.server.logic.executors.plugin.marbs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ourgrid.common.specification.exception.JobSpecificationException;
import org.ourgrid.common.specification.exception.TaskSpecificationException;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.marbs.gui.model.MarbsJobRequest;
import org.ourgrid.portal.client.plugin.marbs.to.model.MarbsInputFileTO;
import org.ourgrid.portal.client.plugin.marbs.to.response.MarbsJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.marbs.to.service.MarbsJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;


public class MarbsJobSubmissionExecutor extends JobSubmissionExecutor {
	
	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	private Long    uploadId;
	private File    SolosFile;
	private File    culturasFile;
	private File    preipitacaoFile;
	private String  type;
	
	
	public Long getUploadId() {
		return uploadId;
	}
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}
	public File getSolosFile() {
		return SolosFile;
	}
	public void setSolosFile(File solosFile) {
		SolosFile = solosFile;
	}
	public File getCulturasFile() {
		return culturasFile;
	}
	public void setCulturasFile(File culturasFile) {
		this.culturasFile = culturasFile;
	}
	public File getPreipitacaoFile() {
		return preipitacaoFile;
	}
	public void setPreipitacaoFile(File preipitacaoFile) {
		this.preipitacaoFile = preipitacaoFile;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MarbsJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	@Override
	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		MarbsJobSubmissionTO marbsJobSubmissionTO = (MarbsJobSubmissionTO) serviceTO;
		MarbsJobSubmissionResponseTO MarbsJobSubmissionResponseTO = new MarbsJobSubmissionResponseTO();
		
		initializeClient();
		
		uploadId = marbsJobSubmissionTO.getUploadId();
		String userLogin = marbsJobSubmissionTO.getUserLogin();
		boolean emailNotification = marbsJobSubmissionTO.isEmailNotification();
		MarbsInputFileTO inputFile = marbsJobSubmissionTO.getInputFile();
		
		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(inputFile, marbsJobSubmissionTO);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = addJob(marbsJobSubmissionTO.getUserLogin(), theJob);
		
		MarbsJobSubmissionResponseTO.setJobID(jobId);
		MarbsJobSubmissionResponseTO.getJobIDs().add(jobId);

		addRequest(jobId, new MarbsJobRequest(jobId, uploadId, userLogin, emailNotification, inputFile));
		reeschedule();

		return MarbsJobSubmissionResponseTO;
		
	}
	
	private String getFilesPath() {

		return getPortal().getDAOManager().getUploadDirName(uploadId);
	}
	
	private JobSpecification compileInputs(MarbsInputFileTO inputFile, MarbsJobSubmissionTO marbsJobSubmissionTO) throws JobSpecificationException, TaskSpecificationException, IOException, ExecutionException {

		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();
		
		setType("");

		File remoteFolder = new File(getFilesPath());
		
		for (String file : remoteFolder.list()) {
			if(file.endsWith(".sol")) {
				setSolosFile(new File(remoteFolder.getAbsolutePath() + FILE_SEPARATOR + file));
			} else if(file.endsWith(".ras")) {
				setCulturasFile(new File(remoteFolder.getAbsolutePath() + FILE_SEPARATOR + file));
			}else if(file.endsWith(".pmh")) {
				setPreipitacaoFile(new File(remoteFolder.getAbsolutePath() + FILE_SEPARATOR + file));
			}

		}
		
		String outputFile = "output.txt";
		createJobSpecification(tasksList, outputFile, remoteFolder.getAbsolutePath() + FILE_SEPARATOR + "output");
		
		return new JobSpecification("Marbs Job", "", tasksList);
	}
	
	private void createJobSpecification(List<TaskSpecification> tasksList, String outputFile,String outputPath) throws TaskSpecificationException, ExecutionException {
		
		String marbsFolderPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.MARBS_PATH);
		String marbsPath = marbsFolderPath + FILE_SEPARATOR + "marbs.jar";
		String balHidPath = marbsFolderPath + FILE_SEPARATOR + "balhid.jar"; 
		
		File marbsFilePath = new File (marbsPath);
		File balHidFilePath = new File(balHidPath); 
		try {
			marbsPath = marbsFilePath.getCanonicalPath();
			balHidPath = balHidFilePath.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		
		if(verifyFiles()) {
			// create initBlock
			IOBlock initBlock = new IOBlock();
			IOEntry marbsFile = new IOEntry("put", marbsPath, "marbs.jar");
			IOEntry balHidFile = new IOEntry("put", balHidPath, "balhid.jar");
			IOEntry solosFile = new IOEntry("put", getSolosFile().getAbsolutePath(), getSolosFile().getName());
			IOEntry culturasFile = new IOEntry("put", getCulturasFile().getAbsolutePath(), getCulturasFile().getName());
			IOEntry precipitacaoFile = new IOEntry("put", getPreipitacaoFile().getAbsolutePath(), getPreipitacaoFile().getName());
			
			
			initBlock.putEntry(marbsFile);
			initBlock.putEntry(balHidFile);
			initBlock.putEntry(solosFile);
			initBlock.putEntry(culturasFile);
			initBlock.putEntry(precipitacaoFile);
			
			//create remotExe
			String remoteExe = "";
			StringBuilder command = new StringBuilder();
			command.append("java -jar marbs.jar ");
			command.append(getPreipitacaoFile().getName());
			command.append(" ");
			command.append(getSolosFile().getName());
			command.append(" ");
			command.append(getCulturasFile().getName());
			command.append(" ");
			command.append("output.txt");
			remoteExe = command.toString();
			

			//create finalBlock
			IOBlock finalBlock = new IOBlock();
			IOEntry finalEntry = new IOEntry("get",outputFile, outputPath + FILE_SEPARATOR +outputFile );
			finalBlock.putEntry(finalEntry);
			
			
			TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
			tasksList.add(taskSpec);
		}
	}
	
	private boolean verifyFiles() {
		return (getSolosFile() != null && getCulturasFile() != null && getPreipitacaoFile() != null); 
	}



}
