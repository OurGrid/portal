package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.service.DeleteUploadStoreTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public class DeleteUploadStoreExecutor extends AbstractExecutor {

	public DeleteUploadStoreExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		DeleteUploadStoreTO to = (DeleteUploadStoreTO) serviceTO;
		
		String uploadDirName = getPortal().getDAOManager().getUploadDirName(to.getUploadId());
		
		File uploadFileDir = new File(uploadDirName);
		
		if (uploadFileDir.exists()) {
			uploadFileDir.delete();
		}
		
		ResponseTO responseTO = new ResponseTO();
		return responseTO;
	}

	public void logTransaction(ServiceTO serviceTO) {
		
		DeleteUploadStoreTO to = (DeleteUploadStoreTO) serviceTO;
		
		String uploadDirName = getPortal().getDAOManager().getUploadDirName(to.getUploadId());
		
		getLogger().info("Executor Name: Delete Upload Store" + LINE_SEPARATOR +
				"Parameters: " + "Upload ID: " + to.getUploadId() + LINE_SEPARATOR +
				"Upload Directory: " + uploadDirName);
	}
}