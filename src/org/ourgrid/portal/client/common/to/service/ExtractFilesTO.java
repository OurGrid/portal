package org.ourgrid.portal.client.common.to.service;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class ExtractFilesTO extends ServiceTO{

	private static final long serialVersionUID = -2197534586092940620L;

	private Long uploadId;
	
	private FileTO fileTO;

	private String userName;
	
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public Long getUploadId() {
		return uploadId;
	}

	public void setFileTO(FileTO fileTO) {
		this.fileTO = fileTO;
	}
	
	public FileTO getFileTO(){
		return fileTO;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName(){
		return userName;
	}
}