package org.ourgrid.portal.client.common.to.service;

public class JobSubmissionTO extends ServiceTO {

	private static final long serialVersionUID = -6611551079605888649L;
	
	private Long uploadId;
	
	private String userLogin;
	
	private String jobName;
	
	private boolean emailNotification;

	private String location;
	
	public JobSubmissionTO() {
		super();
	}

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getJdfName() {
		return jobName;
	}

	public void setJobName(String jdfName) {
		this.jobName = jdfName;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public void setLocation(String value) {
		this.location = value;
	}
	
	public String getLocation() {
		return location;
	}
}