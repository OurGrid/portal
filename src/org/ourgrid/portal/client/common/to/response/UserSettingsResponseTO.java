package org.ourgrid.portal.client.common.to.response;

import org.ourgrid.portal.client.common.to.model.UserTO;


public class UserSettingsResponseTO extends ResponseTO {
	
	private static final long serialVersionUID = 2264696823017855831L;

	private UserTO loggedUser;
	
	public UserSettingsResponseTO() {
		super();
	}
	
	public UserTO getUser() {
		return loggedUser;
	}

	public void setUser(UserTO loggedUser) {
		this.loggedUser = loggedUser;
	}
	
}
