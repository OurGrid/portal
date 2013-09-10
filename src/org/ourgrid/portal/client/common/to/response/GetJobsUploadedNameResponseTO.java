package org.ourgrid.portal.client.common.to.response;

import java.util.List;

public class GetJobsUploadedNameResponseTO extends ResponseTO {

	private static final long serialVersionUID = 5601597340179321003L;

	private List<String> jdfNames;

	public List<String> getJdfNames() {
		return jdfNames;
	}

	public void setJdfNames(List<String> jdfNames) {
		this.jdfNames = jdfNames;
	}
}
