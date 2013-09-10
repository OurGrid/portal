package org.ourgrid.portal.server.logic.interfaces;

import org.apache.log4j.Logger;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.model.DAOManager;

public interface OurGridPortalIF {
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException;

	public DAOManager getDAOManager();
	
	public BrokerPortalAsyncApplicationClient getBrokerPortalClient();

	public Logger getLogger();
	
}