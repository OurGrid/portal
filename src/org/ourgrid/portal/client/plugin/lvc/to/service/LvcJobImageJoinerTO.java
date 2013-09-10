package org.ourgrid.portal.client.plugin.lvc.to.service;

import org.ourgrid.portal.client.common.to.service.ServiceTO;

public class LvcJobImageJoinerTO extends ServiceTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6337410818471055512L;
	
	private String outputDir;
	
	private Integer jobId;

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
	
	
}
