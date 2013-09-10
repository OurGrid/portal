package org.ourgrid.portal.server.logic.executors.to.service;

import org.ourgrid.portal.client.common.to.service.ServiceTO;

public class DeleteUploadStoreTO extends ServiceTO {

	private static final long serialVersionUID = -2197534586092940620L;

	private long uploadId;

	public long getUploadId() {
		return uploadId;
	}

	public void setUploadId(long uploadId) {
		this.uploadId = uploadId;
	}
	
}