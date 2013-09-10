package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.util.Date;

import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.NewFolderResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.NewFolderTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class NewFolderExecutor extends AbstractExecutor {

	public NewFolderExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		NewFolderTO newFolderTO = (NewFolderTO) serviceTO;
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		File newDirectory = new File(storagePath + newFolderTO.getRoot().getLocation() + newFolderTO.getFile());

		if(!newDirectory.exists()){
			newDirectory.mkdirs();
		}else{
			throw new ExecutionException(OurGridPortalServiceMessages.NEW_FOLDER_NAME_ERROR);
		}

		String location = newFolderTO.getRoot().getLocation() + newFolderTO.getFile() + File.separator;
		
		FileTO fileTO = new FileTO(newDirectory.getName(), location, true);
		fileTO.setDate(new Date(newDirectory.lastModified()));
		fileTO.setHasChildren(false);
		
		NewFolderResponseTO responseTO = new NewFolderResponseTO();
		responseTO.setFile(fileTO);
		
		return responseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		NewFolderTO newFolderTO = (NewFolderTO) serviceTO;
		
		getLogger().info("Executor Name: New Folder" + LINE_SEPARATOR +
				"Parameters: " + "File Name: " + newFolderTO.getFile() + LINE_SEPARATOR + 
				"Location:" + newFolderTO.getRoot().getLocation());
	}
}