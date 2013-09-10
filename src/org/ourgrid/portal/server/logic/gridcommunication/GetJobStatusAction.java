package org.ourgrid.portal.server.logic.gridcommunication;

import java.io.Serializable;
import java.util.List;

import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

public class GetJobStatusAction implements RepeatedAction {
	
	private List<Integer> jobIds;

	public GetJobStatusAction(List<Integer> jobIds) {
		this.jobIds = jobIds;
	}

	public void run(Serializable arg0, ServiceManager serviceManager) {
		BrokerPortalAsyncApplicationClient applicationClient = (BrokerPortalAsyncApplicationClient) serviceManager.getApplication();
		applicationClient.getJobsStatus(jobIds);
	}

}
