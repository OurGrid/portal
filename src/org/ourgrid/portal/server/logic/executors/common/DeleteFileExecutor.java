package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.DeleteFileResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.DeleteFileTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.CheckQuotaResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.CheckQuotaTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class DeleteFileExecutor extends AbstractExecutor {

	private static final String QUOTA_VERIFICATION_ERROR = "Quota verification error";
	
	public DeleteFileExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		DeleteFileTO deleteFileTO = (DeleteFileTO) serviceTO;
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		String userName = deleteFileTO.getUserName();
		
		deleteFiles(deleteFileTO, storagePath);
		
		CheckQuotaResponseTO checkQuotaResponseTO = null;
		try {
			checkQuotaResponseTO = verifyQuota(userName);
		} catch (Exception e) {
		}
		
		if(checkQuotaResponseTO == null){
			throw new ExecutionException(QUOTA_VERIFICATION_ERROR);
		}

		DeleteFileResponseTO responseTO = new DeleteFileResponseTO();
		responseTO.setListFiles(deleteFileTO.getListFiles());
		responseTO.setQuotaPercentage(checkQuotaResponseTO.getQuotaPercentage());
		responseTO.setIsQuotaExed(checkQuotaResponseTO.isQuotaExed());
		return responseTO;
	}
	
	private CheckQuotaResponseTO verifyQuota(String userName) throws Exception {
		
		CheckQuotaTO to = new CheckQuotaTO();
		to.setExecutorName(CommonServiceConstants.CHECK_QUOTA_EXECUTOR);
		to.setUserName(userName);
		
		CheckQuotaResponseTO responseTO = 
			(CheckQuotaResponseTO) getPortal().execute(to);
		
		return responseTO;
	}

	private void deleteFiles(DeleteFileTO deleteFileTO, String storagePath)throws ExecutionException {
		
		List<String> locations = new ArrayList<String>();
		
		for (FileTO fileTO : deleteFileTO.getListFiles()) {
			
			String fileName = fileTO.getName();
			String path = deleteFileTO.getRoot().getLocation() + fileName;
			File file = new File(storagePath + path);
			
		    if (!file.exists()){
		    	throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY + fileName );
		    }
		
		    if (!file.canWrite()){
		      throw new  ExecutionException(OurGridPortalServiceMessages.WRITE_PROTECTED + fileName );
		    }
		    
		    boolean success =  deleteRecursive(file);

		    if (!success){
		      throw new ExecutionException(OurGridPortalServiceMessages.DELETE_FILE_ERROR + fileName);
		    }
		    
		    locations.add(path + File.separator);
		}
	}
	
	private boolean deleteRecursive(File path) throws ExecutionException {

		if( path.exists() ) {
			if (path.isDirectory()) {
				File[] files = path.listFiles();
                
				for(int i=0; i<files.length; i++) {
                
					if(files[i].isDirectory()) {
						deleteRecursive(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
        return(path.delete());
	}

	public void logTransaction(ServiceTO serviceTO) {
		
		DeleteFileTO deleteFileTO = (DeleteFileTO) serviceTO;
		
		getLogger().info("Executor Name: Delete File" + LINE_SEPARATOR +
				"Parameters: " + "File Name: " + deleteFileTO.getListFiles() + LINE_SEPARATOR + 
				"Location:" + deleteFileTO.getRoot().getLocation());
	}
}