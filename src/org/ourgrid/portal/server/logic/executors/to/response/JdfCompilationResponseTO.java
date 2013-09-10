package org.ourgrid.portal.server.logic.executors.to.response;

import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.JdfCompilationTO;

public class JdfCompilationResponseTO extends ResponseTO {

	private static final long serialVersionUID = 642171175943526359L;

	private JdfCompilationTO jdfValidatorTO;
	private JobSpecification jobSpec;
	
	public void setJobSpecification(JobSpecification jobSpec) {
		this.jobSpec = jobSpec;
	}
	
	public JobSpecification getJobSpecification() {
		return jobSpec;
	}

	public void setJdfValidatorTO(JdfCompilationTO jdfValidatorTO) {
		this.jdfValidatorTO = jdfValidatorTO;
	}

	public JdfCompilationTO getJdfValidatorTO() {
		return jdfValidatorTO;
	}
}