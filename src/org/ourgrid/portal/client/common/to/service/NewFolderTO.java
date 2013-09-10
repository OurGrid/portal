package org.ourgrid.portal.client.common.to.service;

import java.io.Serializable;

import org.ourgrid.portal.client.common.to.model.FileTO;



public class NewFolderTO extends ServiceTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private FileTO root;
	
	public NewFolderTO() {
		
	}
	
	public FileTO getRoot() {
		return root;
	}

	public void setRoot(FileTO root) {
		this.root = root;
	}

	public String getFile() {
		return name;
	}

	public void setFile(String file) {
		this.name = file;
	}
}
