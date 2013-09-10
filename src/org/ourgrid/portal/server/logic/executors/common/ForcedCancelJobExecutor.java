package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ForcedCancelJobTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

public class ForcedCancelJobExecutor extends AbstractExecutor {

	public ForcedCancelJobExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		ForcedCancelJobTO forcedCancelJobTO = (ForcedCancelJobTO) serviceTO;
		
		BrokerPortalAsyncApplicationClient brokerPortalClient = getPortal().getBrokerPortalClient();
		
		if (!brokerPortalClient.isServerApplicationUp()) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		for (AbstractRequest abstractRequest : forcedCancelJobTO.getJobsRequestList()) {
			brokerPortalClient.cancelJob(abstractRequest.getJobID());
		}
		
		ResponseTO responseTO = new ResponseTO();
		responseTO.setMessage(OurGridPortalServiceMessages.CANCEL_JOB_SUCCEED_MSG);
		
		return responseTO;
	}

	public void logTransaction(ServiceTO serviceTO) {
		
		getLogger().info("Executor Name: Forced Cancel Job" + LINE_SEPARATOR);
	}
}