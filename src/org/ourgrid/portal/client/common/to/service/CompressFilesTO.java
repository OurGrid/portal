package org.ourgrid.portal.client.common.to.service;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class CompressFilesTO extends ServiceTO{

	private static final long serialVersionUID = -2197534586092940620L;

	private List<FileTO> file;
	
	private String userName;
	
	public void setFile(List<FileTO> file) {
		this.file = file;
	}

	public List<FileTO> getFile() {
		return file;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}

}