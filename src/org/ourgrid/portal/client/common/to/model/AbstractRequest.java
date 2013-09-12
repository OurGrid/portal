package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;

public abstract class AbstractRequest implements Serializable, Comparable<AbstractRequest> {

	private static final long serialVersionUID = 2268180193921968365L;
	
	private int jobID;
	private long uploadId;
	private String userLogin;
	private boolean emailNotification;
	private String jobType;

	public AbstractRequest(int jobID, long uploadId, String userLogin, boolean emailNotification, String jobType) {
		this.setJobID(jobID);
		this.setUploadID(uploadId);
		this.setUserLogin(userLogin);
		this.setEmailNotification(emailNotification);
		this.setJobType(jobType);
	}
	
	public AbstractRequest() {}

	private void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobType() {
		return jobType;
	}

	private void setJobID(int jobID){
		this.jobID = jobID;
	}
	
	private void setUploadID(long uploadID){
		this.uploadId = uploadID;
	}
	
	public int getJobID() {
		return this.jobID;
	}
	
	public long getUploadId() {
		return uploadId;
	}
	
	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (emailNotification ? 1231 : 1237);
		result = prime * result + (int) (jobID ^ (jobID >>> 32));
		result = prime * result + (int) (uploadId ^ (uploadId >>> 32));
		result = prime * result
				+ ((userLogin == null) ? 0 : userLogin.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractRequest other = (AbstractRequest) obj;
		if (emailNotification != other.emailNotification)
			return false;
		if (jobID != other.jobID)
			return false;
		if (uploadId != other.uploadId)
			return false;
		if (userLogin == null) {
			if (other.userLogin != null)
				return false;
		} else if (!userLogin.equals(other.userLogin))
			return false;
		return true;
	}

	public void deactivateNotification() {
		setEmailNotification(false);
	}

	public int compareTo(AbstractRequest o) {
		return jobID - o.getJobID();
	}
}