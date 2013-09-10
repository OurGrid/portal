package org.ourgrid.portal.client.plugin.marbs.to.model;

import java.io.Serializable;

public class MarbsInputFileTO implements Serializable {

	
private static final long serialVersionUID = 1191416954967863721L;
	
	private Long    uploadID;

	public Long getUploadID() {
		return uploadID;
	}

	public void setUploadID(Long uploadID) {
		this.uploadID = uploadID;
	}
	
}
