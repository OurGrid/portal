package org.ourgrid.portal.client.common.to.response;

import java.io.Serializable;


public class CheckProxyResponseTO extends ResponseTO implements Serializable {
	
	private static final long serialVersionUID = 3364738242452557570L;

	private Boolean proxyExists;
	
	private Long timeLeft;

	public void setProxyExists(Boolean proxyExists) {
		this.proxyExists = proxyExists;
	}

	public Boolean getProxyExists() {
		return proxyExists;
	}

	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
	}

	public Long getTimeLeft() {
		return timeLeft;
	}
	
	
}