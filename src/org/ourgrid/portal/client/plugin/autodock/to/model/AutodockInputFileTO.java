package org.ourgrid.portal.client.plugin.autodock.to.model;

import java.io.Serializable;

public class AutodockInputFileTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String typeValue = "ResultTO";
	
	private String name;
	private Long uploadId;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUploadId(){
		return uploadId;
	}
	
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}
}