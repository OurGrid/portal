package org.ourgrid.portal.client.common.to.service;


public class UserSettingsTO extends ServiceTO {

	private static final long serialVersionUID = -5375463212553027341L;

	private String password;
	private String email;
	private String login;
	private String newPassword;
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
}
