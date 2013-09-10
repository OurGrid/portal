package org.ourgrid.portal.client.common.to.service;

import java.io.Serializable;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class RenameFileTO extends ServiceTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private FileTO file;

	public RenameFileTO() {
	}
	
	public FileTO getFile() {
		return file;
	}

	public void setFile(FileTO file) {
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}