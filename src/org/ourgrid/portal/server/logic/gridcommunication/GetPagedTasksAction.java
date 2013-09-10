package org.ourgrid.portal.server.logic.gridcommunication;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

public class GetPagedTasksAction implements RepeatedAction {
	
	private Map<Integer, List<PagedTaskRequest>> pagedTasksRequests;

	public GetPagedTasksAction(Map<Integer, List<PagedTaskRequest>> pagedTasksRequests) {
		this.pagedTasksRequests = pagedTasksRequests;
	}

	public void run(Serializable arg0, ServiceManager serviceManager) {
		BrokerPortalAsyncApplicationClient applicationClient = (BrokerPortalAsyncApplicationClient) serviceManager.getApplication();
		
		for (Entry<Integer, List<PagedTaskRequest>> entry : pagedTasksRequests.entrySet()) {
			Integer jobId = entry.getKey();
			 List<PagedTaskRequest> requests = entry.getValue();
			 
			 for (PagedTaskRequest request : requests) {
				 applicationClient.getPagedTasks(jobId, request.getPageOffset(), request.getPageSize());
			}
		}
	}

}
