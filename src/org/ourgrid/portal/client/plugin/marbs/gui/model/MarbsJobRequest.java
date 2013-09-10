package org.ourgrid.portal.client.plugin.marbs.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;
import org.ourgrid.portal.client.plugin.marbs.to.model.MarbsInputFileTO;

public class MarbsJobRequest extends AbstractRequest implements JobRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8096064503521605194L;
	private MarbsInputFileTO inputFile;
	
	public MarbsJobRequest() {
		super();
	}
	
	public MarbsJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			MarbsInputFileTO inputFiles) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.MARBS_JOB);
		
		this.setInputFile(inputFiles);
	}

	public void setInputFile(MarbsInputFileTO inputFile) {
		this.inputFile = inputFile;
	}

	public MarbsInputFileTO getInputFile() {
		return inputFile;
	}

}
