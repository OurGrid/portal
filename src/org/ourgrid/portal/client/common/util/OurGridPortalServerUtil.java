package org.ourgrid.portal.client.common.util;

import org.ourgrid.portal.client.OurGridPortalService;
import org.ourgrid.portal.client.OurGridPortalServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class OurGridPortalServerUtil {
	
	private static OurGridPortalServiceAsync portalService = null;
	
	public static OurGridPortalServiceAsync getInstance() {
		if(portalService == null){

			portalService = GWT.create(OurGridPortalService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) portalService;
			String moduleRelativeURL = GWT.getModuleBaseURL() + "OurGridPortalService";
			endpoint.setServiceEntryPoint(moduleRelativeURL);
		}
		return portalService;
	}

}
