package org.ourgrid.portal.client.common.to.service;

public class GetJobsUploadedNameTO extends ServiceTO {

	private static final long serialVersionUID = -2987897189503560498L;
	
	private Long uploadSessionId;

	public Long getUploadSessionId() {
		return uploadSessionId;
	}

	public void setUploadSessionId(Long uploadSessionId) {
		this.uploadSessionId = uploadSessionId;
	}

}
