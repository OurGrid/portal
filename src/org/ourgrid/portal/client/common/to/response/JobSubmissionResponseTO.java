package org.ourgrid.portal.client.common.to.response;

import java.util.ArrayList;
import java.util.List;




public class JobSubmissionResponseTO extends ResponseTO {

	private static final long serialVersionUID = 2251204728988726972L;
	
	private Integer jobID;
	private List<Integer> jobIDs;
	
	
	public JobSubmissionResponseTO() {
		super();
		this.jobID = -1;
		jobIDs = new ArrayList<Integer>();
	}

	public Integer getJobID() {
		return jobID;
	}

	public void setJobID(Integer jobID) {
		this.jobID = jobID;
	}

	public List<Integer> getJobIDs() {
		return jobIDs;
	}

	public void setJobIDs(List<Integer> jobIDs) {
		this.jobIDs = jobIDs;
	}

}
