package org.ourgrid.portal.client.common.to.response;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.UserTO;


public class UsersAccountsResponseTO extends ResponseTO {

	private static final long serialVersionUID = 2251204728988726972L;
	
	private List<UserTO> result;
	
	public UsersAccountsResponseTO() {
		super();
	}
	
	public List<UserTO> getResult() {
		return result;
	}
	
	public void setResult(List<UserTO> newResult) {
		this.result = newResult;
	}

}
