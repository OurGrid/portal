package org.ourgrid.portal.client.common.to.service;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.UserTO;

public class UserApprovalTO extends ServiceTO {

	private static final long serialVersionUID = -6611551079605888649L;
	
	private List<UserTO> listUser;
	
	public UserApprovalTO() {
		super();
	}

	public void setUserSelection(List<UserTO> listUser) {
		this.listUser = listUser;
	}
	
	public List<UserTO> getUserSelection() {
		return this.listUser;
	}
}