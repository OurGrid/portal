package org.ourgrid.portal.client.common.to.response;

import org.ourgrid.portal.client.common.to.model.FileTO;


public class RenameFileResponseTO extends ResponseTO {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String oldLocation;

	private String newLocation;
	
	private FileTO fileTO;
	
	private String newName;
	
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public RenameFileResponseTO() {
		super();
	}

	public String getOldLocation() {
		return oldLocation;
	}

	public void setOldLocation(String oldLocation) {
		this.oldLocation = oldLocation;
	}

	public String getNewLocation() {
		return newLocation;
	}

	public void setNewLocation(String newLocation) {
		this.newLocation = newLocation;
	}

	public FileTO getOldFileTO() {
		return fileTO;
	}

	public void setOldFileTO(FileTO oldFileTO) {
		this.fileTO = oldFileTO;
	}
}