package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;

import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.SimpleOurGridJobRequest;
import org.ourgrid.portal.client.common.to.response.JobSubmissionResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.JdfCompilationResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.JdfCompilationTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class OurGridJobSubmissionExecutor extends JobSubmissionExecutor {

	public OurGridJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		JobSubmissionTO jobSubmissionTO = (JobSubmissionTO) serviceTO;
		JobSubmissionResponseTO jobSubmissionResponseTO = new JobSubmissionResponseTO();
		
		initializeClient();
		
		Long uploadId = jobSubmissionTO.getUploadId();
		String jdfName = jobSubmissionTO.getJdfName();
		String userLogin = jobSubmissionTO.getUserLogin();
		String location = jobSubmissionTO.getLocation();
		boolean emailNotification = jobSubmissionTO.isEmailNotification();
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		File fileJdf;
		if(location == null){
			fileJdf = getPortal().getDAOManager().getStoredFileByName(uploadId, jdfName);
		}else{
			fileJdf = new File(storagePath + location + jdfName);
		}
		
		if (fileJdf == null) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		
		JobSpecification theJob = compileJdf(fileJdf);

		Integer jobId = addJob(jobSubmissionTO.getUserLogin(), theJob);
		
		jobSubmissionResponseTO.setJobID(jobId);
		jobSubmissionResponseTO.setMessage(OurGridPortalServiceMessages.SUBMIT_JDF_SUCCEED_MSG);
		
		addRequest(jobId, new SimpleOurGridJobRequest(jobId, uploadId, userLogin, emailNotification));
		reeschedule();
		
		return jobSubmissionResponseTO;
	}

	private JobSpecification compileJdf(File fileJdf) throws ExecutionException {
		JdfCompilationTO jdfCompilationTO = createJdfCompilationTO(fileJdf);

		JdfCompilationResponseTO jdfCompilationResponseTO =
			(JdfCompilationResponseTO) getPortal().execute(jdfCompilationTO);
		
		return jdfCompilationResponseTO.getJobSpecification();
	}

	private JdfCompilationTO createJdfCompilationTO(File fileJdf) {
		
		JdfCompilationTO jdfCompilationTO = new JdfCompilationTO();
		jdfCompilationTO.setExecutorName(CommonServiceConstants.JDF_COMPILATION_EXECUTOR);
		jdfCompilationTO.setJdfFile(fileJdf);
		
		return jdfCompilationTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		JobSubmissionTO jobSubmissionTO = (JobSubmissionTO) serviceTO;
		
		getLogger().info("Executor Name: Job Submission" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + jobSubmissionTO.getUserLogin() + LINE_SEPARATOR + 
				" UploadId: " + jobSubmissionTO.getUploadId());
	}
}