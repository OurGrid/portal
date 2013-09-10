package org.ourgrid.portal.client.common.to.service;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;

public class ForcedCancelJobTO extends ServiceTO {

	private static final long serialVersionUID = -2987897189503560498L;
	
	private List<AbstractRequest> jobsRequestList;
	
	public List<AbstractRequest> getJobsRequestList() {
		return jobsRequestList;
	}

	public void setJobsRequestList(List<AbstractRequest> jobsRequestList) {
		this.jobsRequestList = jobsRequestList;
	}
}