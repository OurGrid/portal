package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;

public class UserTO implements Serializable {
	
	private static final long serialVersionUID = -6133813112368614663L;
	
	private String login;
	private String passw;
	private String email;
	private String profile;
	private Boolean pending;
	private Integer storageSize;
	
	public UserTO() {}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return passw;
	}

	public void setPassword(String passw) {
		this.passw = passw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Boolean getPending() {
		return pending;
	}

	public void setPending(Boolean pending) {
		this.pending = pending;
	}

	public void setStorageSize(Integer storageSize) {
		this.storageSize = storageSize;
	}

	public Integer getStorageSize() {
		return storageSize;
	}

}