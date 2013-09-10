package org.ourgrid.portal.client.common.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class UserGridModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	public UserGridModel(String login, String email, boolean pending, Integer quota) {
		set("login", login);
		set("email", email);
		set("pending", pending);
		set("quota", quota);
	}
	
	public String getLogin() {
		return get("login");
	}
	
	public void setLogin(String login) {
		set("login", login);
	}
	
	public String getEmail() {
		return get("email");
	}
	
	public void setEmail(String email) {
		set("email", email);
	}
	
	public Boolean getPending() {
		return get("pending");
	}
	
	public void setPending(Boolean pending) {
		set("pending", pending);
	}
	
	public Integer getQuota() {
		return get("quota");
	}
	
	public void setQuota(Integer quota) {
		set("quota", quota);
	}
}