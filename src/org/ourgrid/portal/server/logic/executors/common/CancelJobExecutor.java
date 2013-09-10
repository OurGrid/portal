package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.CancelJobTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

import br.edu.ufcg.lsd.commune.container.control.ControlOperationResult;

public class CancelJobExecutor extends AbstractExecutor {

	public CancelJobExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		CancelJobTO cancelJobTO = (CancelJobTO) serviceTO;
		
		BrokerPortalAsyncApplicationClient brokerPortalClient = getPortal().getBrokerPortalClient();
		
		if (!brokerPortalClient.isServerApplicationUp()) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		ControlOperationResult cancelJobResult = brokerPortalClient.cancelJob(cancelJobTO.getJobId());
		
		if (cancelJobResult.getErrorCause() != null) {
			
			String message = cancelJobResult.getErrorCause().getMessage();
			if (message != null && !message.equals(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG)) {
				throw new ExecutionException(OurGridPortalServiceMessages.CANCEL_JOB_ERROR_MSG);
			}
			throw new ExecutionException(message);
		}
		
		ResponseTO responseTO = new ResponseTO();
		responseTO.setMessage(OurGridPortalServiceMessages.CANCEL_JOB_SUCCEED_MSG);
		
		return responseTO;
	}

	public void logTransaction(ServiceTO serviceTO) {
		
		CancelJobTO cancelJobTO = (CancelJobTO) serviceTO;
		
		getLogger().info("Executor Name: Cancel Job" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" JobId: " + cancelJobTO.getJobId());
	}
}