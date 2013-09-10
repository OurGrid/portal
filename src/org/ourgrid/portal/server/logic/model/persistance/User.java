package org.ourgrid.portal.server.logic.model.persistance;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 2490155601606875471L;
	
	private String login;
	private String passw;
	private String email;
	private String profile;
	private Boolean pending;
	private Integer storageSize;
	
	public User() {}
	
	@Id
	@Column(length = 50)
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Column(length = 50)
	public String getPassw() {
		return passw;
	}
	
	public void setPassw(String passw) {
		this.passw = passw;
	}

	@Column(length = 50)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 50)
	public String getProfile() {
		return profile;
	}
	
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	@Column
	public Boolean isPending() {
		return pending;
	}
	
	public void setPending(boolean pending) {
		this.pending = pending;
	}

	@Column
	public Integer getStorageSize() {
		return storageSize;
	}
	
	public void setStorageSize(Integer storageSize) {
		this.storageSize = storageSize;
	}
	
	
}