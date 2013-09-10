package org.ourgrid.portal.client.plugin.lvc.to.response;

import org.ourgrid.portal.client.common.to.response.JobSubmissionResponseTO;

public class LvcJobSubmissionResponseTO extends JobSubmissionResponseTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8834655059305945908L;
	
	private String outputDir;

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

}
