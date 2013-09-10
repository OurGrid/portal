package org.ourgrid.portal.client.plugin.cisternas.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.cisternas.to.model.CisternasInputFileTO;

public class CisternasJobSubmissionTO extends JobSubmissionTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1442610701830638036L;
	
	private CisternasInputFileTO inputFile;

	public void setInputFile(CisternasInputFileTO inputFile) {
		this.inputFile = inputFile;
	}

	public CisternasInputFileTO getInputFile() {
		return inputFile;
	}


}
