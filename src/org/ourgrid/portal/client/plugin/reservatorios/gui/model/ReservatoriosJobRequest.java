package org.ourgrid.portal.client.plugin.reservatorios.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;
import org.ourgrid.portal.client.plugin.reservatorios.to.model.ReservatoriosInputFileTO;

public class ReservatoriosJobRequest extends AbstractRequest implements JobRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8096064503521605194L;
	private ReservatoriosInputFileTO inputFile;
	
	public ReservatoriosJobRequest() {
		super();
	}
	
	public ReservatoriosJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			ReservatoriosInputFileTO inputFiles) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.RESERVATORIOS_JOB);
		
		this.setInputFile(inputFiles);
	}

	public void setInputFile(ReservatoriosInputFileTO inputFile) {
		this.inputFile = inputFile;
	}

	public ReservatoriosInputFileTO getInputFile() {
		return inputFile;
	}
}
