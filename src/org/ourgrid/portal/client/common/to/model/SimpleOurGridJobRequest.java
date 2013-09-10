package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;

import org.ourgrid.portal.client.common.CommonServiceConstants;


public class SimpleOurGridJobRequest extends AbstractRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4270937266037230960L;
	
	public SimpleOurGridJobRequest() {
		super();
	}
	
	public SimpleOurGridJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.SIMPLE_OURGRID_JOB);
	}
	
}