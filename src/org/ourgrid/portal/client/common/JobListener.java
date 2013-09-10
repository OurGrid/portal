package org.ourgrid.portal.client.common;

import org.ourgrid.portal.client.common.to.model.JobTO;

public interface JobListener {
	
	public void jobFinished(JobTO jobTO);

}
