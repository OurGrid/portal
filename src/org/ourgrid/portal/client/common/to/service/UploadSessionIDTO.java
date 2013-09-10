package org.ourgrid.portal.client.common.to.service;

public class UploadSessionIDTO extends ServiceTO {

	private static final long serialVersionUID = -8537522823728867130L;
	
	private String userLogin;
	private String location;
	private boolean proxyCertificate;

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getLocation(){
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public void setProxyCertificate(boolean proxyCertificate) {
		this.proxyCertificate = proxyCertificate;
	}

	public boolean isProxyCertificate() {
		return proxyCertificate;
	}
}