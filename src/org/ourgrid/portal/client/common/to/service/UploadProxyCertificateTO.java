package org.ourgrid.portal.client.common.to.service;

public class UploadProxyCertificateTO extends ServiceTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7761347527275622310L;

	private String userLogin;
	
	private Long uploadId;

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

}
