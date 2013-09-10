package org.ourgrid.portal.client.common.to.service;

public class DeleteJobTO extends ServiceTO {

	private static final long serialVersionUID = -6512864891062848028L;
	
	private Integer jobId;

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getJobId() {
		return jobId;
	}
}