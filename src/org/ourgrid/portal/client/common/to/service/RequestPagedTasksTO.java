package org.ourgrid.portal.client.common.to.service;

public class RequestPagedTasksTO extends ServiceTO {

	private static final long serialVersionUID = -2987897189503560498L;
	
	private Integer jobId;
	
	private Integer offset;
	
	private Integer pageSize;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setLastTaskId(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getJobId() {
		return jobId;
	}
}