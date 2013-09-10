package org.ourgrid.portal.client.common.to.response;

public class UploadSessionIDResponseTO extends ResponseTO {

	private static final long serialVersionUID = -1454761292470287553L;
	
	private Long uploadID;
	private String fileName;
	
	public UploadSessionIDResponseTO() {
		super();
	}

	public Long getUploadId() {
		return uploadID;
	}

	public void setUploadId(Long uploadId) {
		this.uploadID = uploadId;
	}

	public String getFileUploadName() {
		return fileName;
	}
	
	public void setFileUploadName(String fileName) {
		this.fileName = fileName;
	}
}