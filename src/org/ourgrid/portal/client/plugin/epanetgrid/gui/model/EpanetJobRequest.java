package org.ourgrid.portal.client.plugin.epanetgrid.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.epanetgrid.to.model.EpanetInputFileTO;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;

public class EpanetJobRequest extends AbstractRequest implements JobRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4437225545795236960L;
	
	private EpanetInputFileTO inputFile;
	
	public EpanetJobRequest() {
		super();
	}
	
	public EpanetJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			EpanetInputFileTO inputFiles) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.EPANET_JOB);
		
		this.setInputFile(inputFiles);
	}

	public void setInputFile(EpanetInputFileTO inputFile) {
		this.inputFile = inputFile;
	}

	public EpanetInputFileTO getInputFile() {
		return inputFile;
	}


}
