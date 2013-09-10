package org.ourgrid.portal.client;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The client side stub for the RPC service.
 */
public interface OurGridPortalService extends RemoteService {
	
	public <T extends ResponseTO> T execute(ServiceTO serviceTO) throws OurGridPortalException; 
	
}
