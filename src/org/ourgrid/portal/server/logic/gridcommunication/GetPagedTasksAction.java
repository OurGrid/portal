package org.ourgrid.portal.server.logic.gridcommunication;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

public class GetPagedTasksAction implements RepeatedAction {
	
	public void run(Serializable arg0, ServiceManager serviceManager) {
//		BrokerPortalAsyncApplicationClient applicationClient = (BrokerPortalAsyncApplicationClient) serviceManager.getApplication();
//		BrokerPortalModel model = applicationClient.getModel();
//		
//		for (Entry<Integer, List<PagedTaskRequest>> entry : model.getPagedTaskRequests().entrySet()) {
//			Integer jobId = entry.getKey();
//			 List<PagedTaskRequest> requests = entry.getValue();
//			 
//			 for (PagedTaskRequest request : requests) {
//				 applicationClient.getPagedTasks(jobId, request.getPageOffset(), request.getPageSize());
//			}
//		}
	}

}
