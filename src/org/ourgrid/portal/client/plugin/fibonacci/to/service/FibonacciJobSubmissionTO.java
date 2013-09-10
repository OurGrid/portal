package org.ourgrid.portal.client.plugin.fibonacci.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;

public class FibonacciJobSubmissionTO extends JobSubmissionTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3088859512144994696L;
	
	private Integer numberOfTasks;

	public void setNumberOfTasks(Integer numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	public Integer getNumberOfTasks() {
		return this.numberOfTasks;
	}
 
	
}
