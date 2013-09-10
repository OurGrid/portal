package org.ourgrid.portal.server.logic.executors.plugin.autodock;

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
import org.ourgrid.portal.client.plugin.autodock.gui.model.AutodockJobRequest;
import org.ourgrid.portal.client.plugin.autodock.to.model.AutodockInputFileTO;
import org.ourgrid.portal.client.plugin.autodock.to.response.AutodockJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.autodock.to.service.AutodockJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

public class AutodockJobSubmissionExecutor extends JobSubmissionExecutor {

	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	private Long    uploadId;
	private File    macromoleculeRigidModelFile;
	private File    macromoleculeFlexibleModelFile;
	private File    gridParametersFile;
	private File    ligantModelFile;
	private File    dockingParametersFile;

	public AutodockJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		AutodockJobSubmissionTO autodockJobSubmissionTO = (AutodockJobSubmissionTO) serviceTO;
		AutodockJobSubmissionResponseTO autodockJobSubmissionResponseTO = new AutodockJobSubmissionResponseTO();

		initializeClient();

		uploadId = autodockJobSubmissionTO.getUploadId();
		String userLogin = autodockJobSubmissionTO.getUserLogin();
		boolean emailNotification = autodockJobSubmissionTO.isEmailNotification();

		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(autodockJobSubmissionTO);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}


		Integer jobId = addJob(autodockJobSubmissionTO.getUserLogin(), theJob);


		autodockJobSubmissionResponseTO.setJobID(jobId);
		autodockJobSubmissionResponseTO.getJobIDs().add(jobId);

		AutodockInputFileTO macromoleculeRigidModel = autodockJobSubmissionTO.getMacromoleculeRigidModelInputFile();
		AutodockInputFileTO macromoleculeFlexibleModel = autodockJobSubmissionTO.getMacromoleculeFlexibleModelInputFile();
		AutodockInputFileTO gridParameters = autodockJobSubmissionTO.getGridParametersInputFile();
		AutodockInputFileTO ligantModel = autodockJobSubmissionTO.getLigantModelInputFile();
		AutodockInputFileTO dockingParameters = autodockJobSubmissionTO.getDockingParametersInputFile();

		addRequest(jobId, new AutodockJobRequest(jobId, uploadId, userLogin, emailNotification, 
				macromoleculeRigidModel, macromoleculeFlexibleModel, gridParameters, ligantModel, dockingParameters));
		reeschedule();

		return autodockJobSubmissionResponseTO;
	}

	private JobSpecification compileInputs(AutodockJobSubmissionTO input) throws JobSpecificationException, TaskSpecificationException, IOException {

		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();

		File remoteFolder = new File(getFilesPath());
		for (String filename : remoteFolder.list()) {
			if(filename.equals(input.getMacromoleculeRigidModelInputFile().getName())) {
				macromoleculeRigidModelFile = createFile(remoteFolder, filename);
			} else if (filename.equals(input.getMacromoleculeFlexibleModelInputFile().getName())) {
				macromoleculeFlexibleModelFile = createFile(remoteFolder, filename);
			} else if (filename.equals(input.getGridParametersInputFile().getName())) {
				gridParametersFile = createFile(remoteFolder, filename);
			} else if (filename.equals(input.getLigantModelInputFile().getName())) {
				ligantModelFile = createFile(remoteFolder, filename);
			} else if (filename.equals(input.getDockingParametersInputFile().getName())) {
				dockingParametersFile = createFile(remoteFolder, filename);
			}
		}

		String outputFile = "output.zip";

		TaskSpecification task = createTask(outputFile, remoteFolder.getAbsolutePath() + FILE_SEPARATOR + "output" );
		tasksList.add(task);

		return new JobSpecification("Autodock Job", "", tasksList);
	}

	private TaskSpecification createTask(String outputFile, String outputPath) throws TaskSpecificationException {

		TaskSpecification taskSpec = null;

		if (validateFiles()) {
			// create initBlock
			IOBlock initBlock = new IOBlock();
			IOEntry macromoleculeRigidModelEntry = new IOEntry("put", macromoleculeRigidModelFile.getAbsolutePath(), macromoleculeRigidModelFile.getName());
			IOEntry macromoleculeFlexibleModelEntry = new IOEntry("put", macromoleculeFlexibleModelFile.getAbsolutePath(), macromoleculeFlexibleModelFile.getName());
			IOEntry gridParametersEntry = new IOEntry("put", gridParametersFile.getAbsolutePath(), gridParametersFile.getName());
			IOEntry ligantModelEntry = new IOEntry("put", ligantModelFile.getAbsolutePath(), ligantModelFile.getName());
			IOEntry dockingParametersEntry = new IOEntry("put", dockingParametersFile.getAbsolutePath(), dockingParametersFile.getName());


			initBlock.putEntry(macromoleculeRigidModelEntry);
			initBlock.putEntry(macromoleculeFlexibleModelEntry);
			initBlock.putEntry(gridParametersEntry);
			initBlock.putEntry(ligantModelEntry);
			initBlock.putEntry(dockingParametersEntry);

			//create remotExe
			String remoteExe = "autogrid4 -p " + gridParametersFile.getName() + 
								" -l " + getFileName(gridParametersFile.getName()) + ".glg;" +
								"autodock4 -p " + dockingParametersFile.getName() + 
								" -l " + getFileName(dockingParametersFile.getName()) + ".dlg;" +
								"zip output.zip *.glg *.gpf *.pdbqt *.map *.maps.fld *.maps.xyz *.dlg *.dpf";


			//create finalBlock
			IOBlock finalBlock = new IOBlock();
			IOEntry finalEntry = new IOEntry("get",outputFile, outputPath + FILE_SEPARATOR +outputFile );
			finalBlock.putEntry(finalEntry);


			taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
		}

		return taskSpec;
	}

	private String getFileName(String inputFileName){
		return inputFileName.substring(0,inputFileName.lastIndexOf(".")).toLowerCase();
	}
	
	private File createFile(File fileIntoFolder, String file) {
		return new File(fileIntoFolder.getAbsolutePath() + FILE_SEPARATOR + file);
	}

	private String getFilesPath() {
		return getPortal().getDAOManager().getUploadDirName(uploadId);
	}

	private boolean validateFiles() {
		return (macromoleculeRigidModelFile != null && macromoleculeFlexibleModelFile != null 
				&& gridParametersFile != null && ligantModelFile != null && dockingParametersFile != null); 
	}

	public void logTransaction(ServiceTO serviceTO) {

		AutodockJobSubmissionTO autodockJobSubmissionTO = (AutodockJobSubmissionTO) serviceTO;

		getLogger().info("Executor Name: Autodock Job Submission" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + autodockJobSubmissionTO.getUserLogin() + LINE_SEPARATOR + 
				" UploadId: " + autodockJobSubmissionTO.getUploadId());
	}
}