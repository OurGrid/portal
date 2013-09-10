package org.ourgrid.portal.client.common.to.service;

public class CancelJobTO extends ServiceTO {

	private static final long serialVersionUID = -2987897189503560498L;
	
	private Integer jobId;

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getJobId() {
		return jobId;
	}
}