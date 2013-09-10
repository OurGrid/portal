package org.ourgrid.portal.client.common.to.service;

public class UsersAccountsTO extends ServiceTO {

	private static final long serialVersionUID = -6611551079605888649L;
	
	private int limit;
	private int pageNumber;
	
	public UsersAccountsTO() {
		super();
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