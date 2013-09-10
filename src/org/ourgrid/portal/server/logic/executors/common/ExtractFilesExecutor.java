package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.io.IOException;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ExtractFilesTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.util.ExtractFiles;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class ExtractFilesExecutor extends AbstractExecutor{

	private static final String SUCCESSUFULLY_EXTRACTED = "The file was successfully extracted.";

	public ExtractFilesExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		ExtractFilesTO extractFilesTO = (ExtractFilesTO) serviceTO;
		Long uploadId = extractFilesTO.getUploadId();
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		File compactedFile =  new File(storagePath + extractFilesTO.getFileTO().getLocation() + extractFilesTO.getFileTO().getName());

		ExtractFiles extractor = new ExtractFiles(compactedFile);
		
		try {
			String destination = storagePath + extractFilesTO.getFileTO().getLocation();
			
			extractor.unpack(destination);
		} catch (IOException e) {
			getLogger().error("Error while unpacking file", e);
			throw new ExecutionException("Error while unpacking file");
		}
		
		ResponseTO extractedFilesResponse = new ResponseTO();
		extractedFilesResponse.setMessage(SUCCESSUFULLY_EXTRACTED);
		
		return extractedFilesResponse;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		ExtractFilesTO extractFilesTO = (ExtractFilesTO) serviceTO;
		
		getLogger().info("Executor Name: Extract Files" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" FileName: " + extractFilesTO.getFileTO().getName());
	}
}