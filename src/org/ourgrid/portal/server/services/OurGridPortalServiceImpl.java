package org.ourgrid.portal.server.services;

import org.ourgrid.portal.client.OurGridPortalException;
import org.ourgrid.portal.client.OurGridPortalService;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.logic.BrokerPortalController;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class OurGridPortalServiceImpl extends RemoteServiceServlet 
	implements OurGridPortalService {

	private static final long serialVersionUID = 7398760674767860490L;

	@SuppressWarnings("unchecked")
	public <T extends ResponseTO> T execute(ServiceTO serviceTO) throws OurGridPortalException {
		
		ResponseTO responseTO = null;
		OurGridPortalIF portal = BrokerPortalController.getInstance();
		
		try {
			responseTO = portal.execute(serviceTO);
		} catch (Throwable e) {
			portal.getLogger().error("Error while processing the service", e);
			throw new OurGridPortalException(e.getMessage());
		}
		
		return (T) responseTO;
	}
}