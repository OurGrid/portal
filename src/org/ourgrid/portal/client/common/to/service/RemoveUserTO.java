package org.ourgrid.portal.client.common.to.service;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.UserTO;

public class RemoveUserTO extends ServiceTO {

	private static final long serialVersionUID = 1L;
	
	private List<UserTO> listUser;
	private int limit;
	private int pageNumber;
	
	public RemoveUserTO() {
		super();
	}

	public void setUserSelection(List<UserTO> list) {
		this.listUser = list;
	}
	
	public List<UserTO> getUserSelection() {
		return this.listUser;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}