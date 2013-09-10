package org.ourgrid.portal.client.plugin.cisternas.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.cisternas.to.model.CisternasInputFileTO;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;

public class CisternasJobRequest extends AbstractRequest implements JobRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8096064503521605194L;
	private CisternasInputFileTO inputFile;
	
	public CisternasJobRequest() {
		super();
	}
	
	public CisternasJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			CisternasInputFileTO inputFiles) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.CISTERNAS_JOB);
		
		this.inputFile = inputFiles;
	}

	public CisternasInputFileTO getInputFile() {
		return inputFile;
	}

	public void setInputFile(CisternasInputFileTO inputFile) {
		this.inputFile = inputFile;
	}
	
}
