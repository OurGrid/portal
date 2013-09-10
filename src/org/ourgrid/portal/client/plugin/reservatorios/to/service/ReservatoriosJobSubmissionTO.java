package org.ourgrid.portal.client.plugin.reservatorios.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.reservatorios.to.model.ReservatoriosInputFileTO;

public class ReservatoriosJobSubmissionTO extends JobSubmissionTO {

	private static final long serialVersionUID = -54470847683102901L;
	
	private ReservatoriosInputFileTO inputFile;
	
	public void setInputFile(ReservatoriosInputFileTO input) {
		this.inputFile = input;
	}
	
	public ReservatoriosInputFileTO getInputFile() {
		return inputFile;
	}

}
