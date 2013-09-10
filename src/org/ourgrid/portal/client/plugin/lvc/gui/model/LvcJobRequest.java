package org.ourgrid.portal.client.plugin.lvc.gui.model;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.model.JobRequest;

public class LvcJobRequest extends AbstractRequest implements JobRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8187652840239587869L;
	

	public LvcJobRequest() {
		super();
	}
	
	public LvcJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification) {
		super(jobID, uploadId, userLogin, emailNotification,CommonServiceConstants.LVC_JOB);
	}
}
