package org.ourgrid.portal.server.logic.executors.to.service;

import org.ourgrid.portal.client.common.to.service.ServiceTO;

public class CheckQuotaExceedWithUploadTO extends ServiceTO {

	private static final long serialVersionUID = -2197534586092940620L;

	private String userName;
	
	private int fileSize;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
}