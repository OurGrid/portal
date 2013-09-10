package org.ourgrid.portal.client.plugin.lvc.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;

public class LvcJobSubmissionTO extends JobSubmissionTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4950520677577858577L;
	
	private int numberOfPartsOnHeight;
	private int numberOfPartsOnWidth;
	
	public int getNumberOfPartsOnHeight() {
		return numberOfPartsOnHeight;
	}
	public void setNumberOfPartsOnHeight(int numberOfPartsOnHeight) {
		this.numberOfPartsOnHeight = numberOfPartsOnHeight;
	}
	public int getNumberOfPartsOnWidth() {
		return numberOfPartsOnWidth;
	}
	public void setNumberOfPartsOnWidth(int numberOfPartsOnWidth) {
		this.numberOfPartsOnWidth = numberOfPartsOnWidth;
	}
	
	
	
}