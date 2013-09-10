package org.ourgrid.portal.client.common.to.service;

import java.io.Serializable;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class FileExplorerTO extends ServiceTO implements Serializable {
	
	private static final long serialVersionUID = 7119147172633339693L;

	private String location;

	private String name;
	
	private String userName;

	private boolean checkQuota;
	
	private FileTO rootTmp;

	public FileExplorerTO() {
		
	}

	public FileTO getRootTmp() {
		return rootTmp;
	}

	public void setRootTmp(FileTO rootTmp) {
		this.rootTmp = rootTmp;
	}



	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getNodeName() {
		return name;
	}
	
	public void setNodeName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}

	public void setCheckQuota(boolean checkQuota) {
		this.checkQuota = checkQuota;
	}
	
	public boolean checkQuota(){
		return checkQuota;
	}
}
