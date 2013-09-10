package org.ourgrid.portal.server.logic.executors.to.response;

import org.ourgrid.portal.client.common.to.response.ResponseTO;

public class CheckQuotaExceedWithUploadResponseTO extends ResponseTO {

	private static final long serialVersionUID = 642171175943526359L;
	
	private boolean exceed;

	public boolean isExceed() {
		return exceed;
	}

	public void setExceed(boolean exceed) {
		this.exceed = exceed;
	}

}