package org.ourgrid.portal.server.logic.executors.plugin.genecodis;

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
import org.ourgrid.portal.client.plugin.genecodis.gui.model.GenecodisJobRequest;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;
import org.ourgrid.portal.client.plugin.genecodis.to.response.GenecodisJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.genecodis.to.service.GenecodisJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class GenecodisJobSubmissionExecutor extends JobSubmissionExecutor {

	private String 	inputFileName;
	private String 	exeFileName;
	private Long 	uploadId;
	private String 	userLogin;
	private boolean emailNotification;

	public GenecodisJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		GenecodisJobSubmissionTO genecodisJobSubmissionTO = (GenecodisJobSubmissionTO) serviceTO;
		GenecodisJobSubmissionResponseTO genecodisJobSubmissionResponseTO = new GenecodisJobSubmissionResponseTO();
		
		String genecodisPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.GENECODIS_PATH);
		
		initializeClient();
		
		List<GenecodisInputFileTO> geneCodisInputFileTO = genecodisJobSubmissionTO.getInputFile();
		
		File file = new File (genecodisPath);
		try {
			genecodisPath = file.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		
		exeFileName 		= genecodisPath;
		userLogin 			= genecodisJobSubmissionTO.getUserLogin();
		uploadId            = genecodisJobSubmissionTO.getUploadId();
		inputFileName 		= extractFileNameFromCompletePath(genecodisJobSubmissionTO.getInputFileName());
		emailNotification 	= genecodisJobSubmissionTO.isEmailNotification();
		
		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(geneCodisInputFileTO);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = addJob(genecodisJobSubmissionTO.getUserLogin(), theJob);
		
		genecodisJobSubmissionResponseTO.setJobID(jobId);
		genecodisJobSubmissionResponseTO.getJobIDs().add(jobId);
		
		addRequest(jobId, new GenecodisJobRequest(jobId, uploadId, userLogin, emailNotification, inputFileName, geneCodisInputFileTO));
		reeschedule();
		
		return genecodisJobSubmissionResponseTO;
	}
	
	private JobSpecification compileInputs(List<GenecodisInputFileTO> inputsFileTO) throws JobSpecificationException, TaskSpecificationException, IOException {
		
		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();
		
		for (GenecodisInputFileTO inputsFile : inputsFileTO) {
		
			Integer genesSupport 		= inputsFile.getGenesSupport();
			Integer analysisType 		= inputsFile.getAnalysisType();
			Integer supportForRandon 	= inputsFile.getSupportForRandom();
			Integer referenceSize		= inputsFile.getReferenceSize();
			Integer selectedRefSize 	= inputsFile.getSelectedRefSize();
			Integer statisticalTest 	= inputsFile.getStatisticalTest();
			Integer correctionMethod	= inputsFile.getCorrectionMethod();
			
			// create initBlock
			IOBlock initBlock = new IOBlock();
			
			IOEntry initEntryExe = new IOEntry("store", exeFileName, "genecode_yang_32_static");
			IOEntry initEntryInput = new IOEntry("put", inputFileName, "my.engene");
			initBlock.putEntry(initEntryExe);
			initBlock.putEntry(initEntryInput);
			
			//create remotExe
			String remoteExe = "chmod +x $STORAGE/genecode_yang_32_static ; $STORAGE/genecode_yang_32_static my.engene " + genesSupport + " -a" + analysisType + " -i" + supportForRandon + " -r" + referenceSize + " -R" + selectedRefSize + " -t" + statisticalTest + " -s" + correctionMethod + " -o results-$JOB.$TASK";
			
			//create finalBlock
			IOBlock finalBlock = new IOBlock();
			IOEntry finalEntry = new IOEntry("get", "results-$JOB.$TASK", getFileName(inputFileName) + "_results-$JOB.$TASK");
			finalBlock.putEntry(finalEntry);
				
			TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
			tasksList.add(taskSpec);
		}

		return new JobSpecification("GeneCodis Job", "", tasksList);
	}

	private String extractFileNameFromCompletePath(String name) {

		File file = new File(name);
		return getPortal().getDAOManager().getUploadDirName(uploadId) + File.separator + file.getName();
	}

	private String getFileName(String inputFileName){
		return inputFileName.substring(0,inputFileName.lastIndexOf(".")).toLowerCase();
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		GenecodisJobSubmissionTO genecodisJobSubmissionTO = (GenecodisJobSubmissionTO) serviceTO;
		
		getLogger().info("Executor Name: Genecodis Job Submission" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + genecodisJobSubmissionTO.getUserLogin() + LINE_SEPARATOR + 
				" UploadId: " + genecodisJobSubmissionTO.getUploadId());
	}

}