package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;

import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.RenameFileResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.RenameFileTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class RenameFileExecutor extends AbstractExecutor {

	public RenameFileExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		RenameFileTO renameFileTO = (RenameFileTO) serviceTO;
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		FileTO fileTO = renameFileTO.getFile();
		String newName = renameFileTO.getName();
		
		
		
		String path = "";
		if(fileTO.isFolder()){
			path = fileTO.getLocation();
		}else{
			path = fileTO.getLocation() + fileTO.getName();
		}
		
		File file = new File(storagePath + path);
		String newPath = "";
		
		if(file.exists()){
			if(file.isDirectory()){
				FileTO parent = (FileTO) fileTO.getParent();
				if(parent != null){
					newPath = parent.getLocation() + newName;
					File newFile = new File(storagePath + newPath);
					if(newFile.exists()){
						throw new ExecutionException(OurGridPortalServiceMessages.RENAME_FOLDER_ERROR);
					}else{
						file.renameTo(new File(storagePath + newPath));
					}
				}else{
					throw new ExecutionException(OurGridPortalServiceMessages.NOT_PERMISSION);
				}
			}else{
			
				newPath = fileTO.getLocation() + newName;
				File newFile = new File(storagePath + newPath);
				if(newFile.exists()){
					throw new ExecutionException(OurGridPortalServiceMessages.RENAME_FILE_ERROR);
				}else{
					file.renameTo(new File(storagePath + newPath));
				}
			}
		}else{
			throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY + fileTO.getName());
		}
		
		RenameFileResponseTO responseTO = new RenameFileResponseTO();
		responseTO.setOldLocation(path);
		responseTO.setNewLocation(newPath + File.separator);
		responseTO.setOldFileTO(fileTO);
		responseTO.setNewName(newName);
		return responseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		RenameFileTO renameFileTO = (RenameFileTO) serviceTO;
		
		getLogger().info("Executor Name: Rename File" + LINE_SEPARATOR +
				"Parameters: " + "File Name: " + renameFileTO.getFile().getName() + LINE_SEPARATOR + 
				"New Name: " + renameFileTO.getName() + LINE_SEPARATOR + 
				"Location:" + renameFileTO.getFile().getLocation());
	}
}