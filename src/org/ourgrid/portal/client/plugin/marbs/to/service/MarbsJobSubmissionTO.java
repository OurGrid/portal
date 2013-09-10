package org.ourgrid.portal.client.plugin.marbs.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.marbs.to.model.MarbsInputFileTO;

public class MarbsJobSubmissionTO extends JobSubmissionTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1074267632433599550L;
	
	private MarbsInputFileTO inputFile;
	
	public void setInputFile(MarbsInputFileTO input) {
		this.inputFile = input;
	}
	
	public MarbsInputFileTO getInputFile() {
		return inputFile;
	}

}
