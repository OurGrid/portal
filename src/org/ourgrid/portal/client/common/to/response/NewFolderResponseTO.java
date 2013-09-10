package org.ourgrid.portal.client.common.to.response;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class NewFolderResponseTO extends ResponseTO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileTO file;
	
	public NewFolderResponseTO() {
		super();
	}

	public FileTO getFile() {
		return file;
	}

	public void setFile(FileTO file) {
		this.file = file;
	}

}