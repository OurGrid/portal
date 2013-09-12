package org.ourgrid.portal.client.common.to.service;

import java.util.List;

public class JobStatusRetrievalTO extends ServiceTO {

	private static final long serialVersionUID = -3915008975103794332L;
	
	private Integer jobId;
	
	private List<Integer> tasksIds;
	
	public JobStatusRetrievalTO () {
		super();
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public List<Integer> getPagesFirstTaskIds() {
		return tasksIds;
	}

	public void setPagesFirstTaskIds(List<Integer> tasksIds) {
		this.tasksIds = tasksIds;
	}
	
	
}