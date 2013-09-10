package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.RequestPagedTasksTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalModel;
import org.ourgrid.portal.server.logic.gridcommunication.PagedTaskRequest;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

public class RequestPagedTasksExecutor extends AbstractExecutor {

	public RequestPagedTasksExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		RequestPagedTasksTO getPagedTasksTO = (RequestPagedTasksTO) serviceTO;
		
		BrokerPortalAsyncApplicationClient brokerPortalClient = getPortal().getBrokerPortalClient();
		
		if (!brokerPortalClient.isServerApplicationUp()) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		PagedTaskRequest pagedTaskRequest = new PagedTaskRequest(getPagedTasksTO.getOffset(), getPagedTasksTO.getPageSize());
		Integer jobId = getPagedTasksTO.getJobId();
		
		BrokerPortalModel model = brokerPortalClient.getModel();
		
		if (!model.containsPagedTaskRequest(jobId, pagedTaskRequest)) {
			model.addPagedTaskRequest(jobId, pagedTaskRequest);
			brokerPortalClient.getManagerClient().reScheduleGetPagedTaskAction();
		}
		
		ResponseTO responseTO = new ResponseTO();
		responseTO.setMessage(OurGridPortalServiceMessages.GET_PAGED_TASKS_SUCCEEDED);

		return responseTO;
	}

	public void logTransaction(ServiceTO serviceTO) {
		
		RequestPagedTasksTO getPagedTasksTO = (RequestPagedTasksTO) serviceTO;
		
		getLogger().info("Executor Name: Paged Tasks" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" JobId: " + getPagedTasksTO.getJobId() + LINE_SEPARATOR +
				" Offset: " + getPagedTasksTO.getOffset() + LINE_SEPARATOR +
				" Page size: " + getPagedTasksTO.getPageSize());
	}
}