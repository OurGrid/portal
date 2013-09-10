package org.ourgrid.portal.client.plugin.fibonacci.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;

public class FibonacciJobRequest extends AbstractRequest implements JobRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8825552184258266400L;
	
	public FibonacciJobRequest() {
		super();
	}
	
	public FibonacciJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification) {
		super(jobID, uploadId, userLogin, emailNotification,CommonServiceConstants.FIBONACCI_JOB);
	}

}
