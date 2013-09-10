package org.ourgrid.portal.client.plugin.cisternas.to.model;

import java.io.Serializable;

public class CisternasInputFileTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8052163262609276524L;
	
	private Long uploadID;
	
	private String type;
	
	private Double initialVolume;
	
	public Long getUploadID() {
		return uploadID;
	}
	
	public void setUploadID(Long uploadID) {
		this.uploadID = uploadID;
	}
	
	public void setType(String type) {
		this.type = type;		
	}

	public String getType() {
		return type;
	}

	public Double getInitialVolume() {
		return initialVolume;
	}

	public void setInitialVolume(Double initialVolume) {
		this.initialVolume = initialVolume;
	}

}
