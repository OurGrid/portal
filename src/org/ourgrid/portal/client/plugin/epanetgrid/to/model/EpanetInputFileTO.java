package org.ourgrid.portal.client.plugin.epanetgrid.to.model;

import java.io.Serializable;

public class EpanetInputFileTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1191416954967863721L;
	
	private Long    uploadID;

	public Long getUploadID() {
		return uploadID;
	}

	public void setUploadID(Long uploadID) {
		this.uploadID = uploadID;
	}
}
