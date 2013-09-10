package org.ourgrid.portal.client;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface OurGridPortalServiceAsync {
	
	public void execute(ServiceTO serviceTO, AsyncCallback<? extends ResponseTO> callback);
	
}
