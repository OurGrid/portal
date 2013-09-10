package org.ourgrid.portal.client.common.to.service;

import org.ourgrid.portal.client.common.CommonServiceConstants;


public class CheckProxyTO extends ServiceTO {

	private static final long serialVersionUID = -5375463212553027341L;

	public CheckProxyTO() {
		setExecutorName(CommonServiceConstants.CHECK_PROXY_EXECUTOR);
	}
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
