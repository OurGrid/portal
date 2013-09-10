package org.ourgrid.portal.client.common.to.service;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.FileTO;



public class DeleteFileTO extends ServiceTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<FileTO> listFiles;
	private FileTO root;
	private String userName;
	
	public DeleteFileTO() {
		
	}
	
	public FileTO getRoot() {
		return root;
	}

	public void setRoot(FileTO root) {
		this.root = root;
	}

	public List<FileTO> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<FileTO> listFile) {
		this.listFiles = listFile;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
}
