package org.ourgrid.portal.server.logic.executors.to.service;

import org.ourgrid.portal.client.common.to.service.ServiceTO;

public class CheckQuotaTO extends ServiceTO {

	private static final long serialVersionUID = -2197534586092940620L;

	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}