package org.ourgrid.portal.server.logic.executors.plugin.fibonacci;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ourgrid.common.specification.exception.JobSpecificationException;
import org.ourgrid.common.specification.exception.TaskSpecificationException;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.fibonacci.gui.model.FibonacciJobRequest;
import org.ourgrid.portal.client.plugin.fibonacci.to.response.FibonacciJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.fibonacci.to.service.FibonacciJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class FibonacciJobSubmissionExecutor extends JobSubmissionExecutor {

	private String exeFileName;

	public FibonacciJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		FibonacciJobSubmissionTO fibonacciJobSubmissionTO = (FibonacciJobSubmissionTO) serviceTO;
		FibonacciJobSubmissionResponseTO fibonacciJobSubmissionResponseTO = new FibonacciJobSubmissionResponseTO();

		Integer numberOfTasks    = fibonacciJobSubmissionTO.getNumberOfTasks();

		String fibonacciPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.FIBONACCI_PATH);

		initializeClient();

		exeFileName = fibonacciPath;
		File file = new File (exeFileName);
		try {
			exeFileName = file.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		String userLogin = fibonacciJobSubmissionTO.getUserLogin();
		boolean emailNotification = fibonacciJobSubmissionTO.isEmailNotification();

		String outputDir = createOutputDir(userLogin);

		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(numberOfTasks, outputDir);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = addJob(fibonacciJobSubmissionTO.getUserLogin(), theJob);
		fibonacciJobSubmissionResponseTO.getJobIDs().add(jobId);
		addRequest(jobId, new FibonacciJobRequest(jobId ,0, userLogin, emailNotification));
		reeschedule();

		return fibonacciJobSubmissionResponseTO;
	}

	private JobSpecification compileInputs(Integer value, String outputDir) throws JobSpecificationException, TaskSpecificationException, IOException {
		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();

		for (int i = 0; i < value; i++) {
			int input = i + 1;
			// create initBlock
			IOBlock initBlock = new IOBlock();
			IOEntry initEntry = new IOEntry("put", exeFileName, "Fibonacci.class");
			initBlock.putEntry(initEntry);

			String remoteExe = "java -cp . Fibonacci output" + input + ".out"+ LINE_SEPARATOR;

			//create finalBlock
			IOBlock finalBlock = new IOBlock();
			IOEntry finalEntry = new IOEntry("get","output" + input + ".out", outputDir + File.separator + "output" + input + ".out" );
			finalBlock.putEntry(finalEntry);

			TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
			tasksList.add(taskSpec);

		}

		return new JobSpecification("Fibonacci Job", "", tasksList);
	}

	private String createOutputDir(String userLogin) {

		String outputDir = "";

		outputDir  = getUploadDirectory();
		outputDir += System.getProperty("file.separator");
		outputDir += userLogin;
		outputDir += System.getProperty("file.separator");
		outputDir += "Fibonnaci Job Submission [" + longToDate() + "]";

		File uploadDirFile = new File(outputDir);
		uploadDirFile.mkdirs();

		return outputDir;
	}

	private String getUploadDirectory() {
		String uploadPath = OurgridPortalProperties.getInstance().
				getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);

		File uploadDirectory = new File(uploadPath);

		if(!uploadDirectory.exists()){
			uploadDirectory.mkdirs();
		}
		return uploadPath;
	}


	private String longToDate() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
		String dateString= dateFormat.format(date);
		return dateString;
	}


	@Override
	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub

	}

}
