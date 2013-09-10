package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.to.model.JobTO;
import org.ourgrid.portal.client.common.to.response.JobStatusRetrievalResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.JobStatusRetrievalTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

public class JobStatusRetrievalExecutor extends AbstractExecutor {

	public JobStatusRetrievalExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		JobStatusRetrievalTO jobStatusTO = (JobStatusRetrievalTO) serviceTO;
		
		Integer jobId = jobStatusTO.getJobId();
		
		if (jobId == null) {
			throw new ExecutionException("Error while trying to retrieve status of a job with null id.");
		}
		
		JobTO jobStatus = getPortal().getBrokerPortalClient().getModel().getStatus(jobId, jobStatusTO.getPagedTasksIds());
		
		if (jobStatus == null) {
			throw new ExecutionException("Error while trying to retrieve job [" + jobId + "] status.");
		}
		
		JobStatusRetrievalResponseTO responseTO = new JobStatusRetrievalResponseTO();
		responseTO.setJobStatus(jobStatus);
		responseTO.setMessage(OurGridPortalServiceMessages.JOB_STATUS_RETRIEVAL_SUCCEEDED);

		return responseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		JobStatusRetrievalTO jobStatusTO = (JobStatusRetrievalTO) serviceTO;
		
		getLogger().info("Executor Name: Job Status Retrieval" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" JobId: " + jobStatusTO.getJobId());
	}
}