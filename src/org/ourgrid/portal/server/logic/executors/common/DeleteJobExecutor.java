package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.DeleteJobTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public class DeleteJobExecutor extends AbstractExecutor {

	public DeleteJobExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		DeleteJobTO deleteJobTO = (DeleteJobTO) serviceTO;
		
		Integer jobId = deleteJobTO.getJobId();
		getPortal().getBrokerPortalClient().getModel().removeJob(jobId);
		getPortal().getDAOManager().removeRequest(jobId);
		
		ResponseTO responseTO = new ResponseTO("Job Removed Successfully");
		return responseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		DeleteJobTO deleteJobTO = (DeleteJobTO) serviceTO;
		
		getLogger().info("Executor Name: Delete Job" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" JobId: " + deleteJobTO.getJobId());
	}
}