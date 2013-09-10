package org.ourgrid.portal.client.plugin.epanetgrid.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.epanetgrid.to.model.EpanetInputFileTO;

public class EpanetJobSubmissionTO extends JobSubmissionTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1074267632433599550L;
	
	private EpanetInputFileTO inputFile;
	
	public void setInputFile(EpanetInputFileTO input) {
		this.inputFile = input;
	}
	
	public EpanetInputFileTO getInputFile() {
		return inputFile;
	}

}
