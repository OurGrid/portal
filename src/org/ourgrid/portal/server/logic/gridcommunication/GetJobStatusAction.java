package org.ourgrid.portal.server.logic.gridcommunication;

import java.io.Serializable;

import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

public class GetJobStatusAction implements RepeatedAction {
	
	public void run(Serializable arg0, ServiceManager serviceManager) {
		BrokerPortalAsyncApplicationClient client = (BrokerPortalAsyncApplicationClient) serviceManager.getApplication();
		BrokerPortalModel model = client.getModel();
		client.getJobsStatus(model.getActiveJobIds());
	}

}
