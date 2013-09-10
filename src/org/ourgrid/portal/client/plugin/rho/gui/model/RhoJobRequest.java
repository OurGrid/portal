package org.ourgrid.portal.client.plugin.rho.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;

public class RhoJobRequest extends AbstractRequest implements JobRequest {
	
	private static final long serialVersionUID = 1845497390213926850L;
	
	
	public RhoJobRequest() {
		super();
	}
	
	public RhoJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification) {
		super(jobID, uploadId, userLogin, emailNotification,CommonServiceConstants.RHO_JOB);
	}
}
