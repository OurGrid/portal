package org.ourgrid.portal.client.common.to.service;

import org.ourgrid.portal.client.common.to.model.UserTO;

public class UserQuotaTO extends ServiceTO{

	private static final long serialVersionUID = -6611551079605888649L;
	
	private UserTO user;
	
	public UserQuotaTO() {
		super();
	}

	public void setUserSelection(UserTO listUser) {
		this.user = listUser;
	}
	
	public UserTO getUser() {
		return this.user;
	}

}
