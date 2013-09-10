package org.ourgrid.portal.client.common.to.response;

import org.ourgrid.portal.client.common.to.model.JobTO;

public class JobStatusRetrievalResponseTO extends ResponseTO {

	private static final long serialVersionUID = -7855222820488713174L;

	public JobTO jobStatus;
	
	public JobStatusRetrievalResponseTO() {
		super();
	}

	public JobTO getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobTO jobStatus) {
		this.jobStatus = jobStatus;
	}
}