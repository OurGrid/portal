package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.CheckQuotaExceedWithUploadResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.CheckQuotaExceedWithUploadTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class CheckQuotaExceedWithUploadExecutor extends AbstractExecutor {

	private static final long BASE_BYTE_MB = 1048576;
	private long storageLength;
	
	public CheckQuotaExceedWithUploadExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		CheckQuotaExceedWithUploadTO to = (CheckQuotaExceedWithUploadTO) serviceTO;
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		String userName = to.getUserName();
		File userDir = new File (storagePath + File.separator + userName);

		boolean exceed = true;
		
		if (userDir.exists()) {
			
			storageLength = userDir.length();
			
			calculeStorageLength(userDir.listFiles(), true);
			
			long newStorageSize = storageLength + to.getFileSize();
			exceed = newStorageSize >= parseMBToByte(getQuotaSize(userName));
		}
		
		CheckQuotaExceedWithUploadResponseTO responseTO = new CheckQuotaExceedWithUploadResponseTO();
		responseTO.setExceed(exceed);
		
		return responseTO;
	}
	
	private void calculeStorageLength(File[] list, boolean isFolder) {
		
		if(list != null){
			for (int i = 0; i < list.length; i++) {
				
				if(!list[i].isHidden()){
					
					if(list[i].isDirectory()){
						calculeStorageLength(list[i].listFiles(), true);
					}else{
						storageLength += list[i].length();
					}
				}
			}
		}
	}

	private Double getQuotaSize(String userName) {
		User uLogin = getPortal().getDAOManager().getUserByLogin(userName);
		Double storageSize = uLogin.getStorageSize().doubleValue();
		return storageSize;
	}

	private double parseMBToByte(double size){
		return size * BASE_BYTE_MB;
	}
	
	
	public void logTransaction(ServiceTO serviceTO) {
		
		CheckQuotaExceedWithUploadTO to = (CheckQuotaExceedWithUploadTO) serviceTO;
		
		getLogger().info("Executor Name: Check Quota Exceed With Upload" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" UserName: " + to.getUserName() + LINE_SEPARATOR +
				" File Upload Size: " + to.getFileSize());
	}
}