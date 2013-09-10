package org.ourgrid.portal.client.plugin.lvc.to.response;

import org.ourgrid.portal.client.common.to.response.ResponseTO;

public class LvcJobImageJoinerResponseTO extends ResponseTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2888844827886954822L;
	
	private boolean finalImageGenerated;

	public boolean isWasFinalImageGenerated() {
		return finalImageGenerated;
	}

	public void setWasFinalImageGenerated(boolean wasFinalImageGenerated) {
		this.finalImageGenerated = wasFinalImageGenerated;
	}
	
}
