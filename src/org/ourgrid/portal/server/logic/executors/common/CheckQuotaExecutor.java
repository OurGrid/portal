package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.text.DecimalFormat;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.CheckQuotaResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.CheckQuotaTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class CheckQuotaExecutor extends AbstractExecutor {

	private static final long BASE_BYTE_MB = 1048576;
	private static final long BASE_BYTE_KB = 1024;
	private static final long BASE_BYTE_GB = 1073741824;
	
	private long storageLength;
	
	public CheckQuotaExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		CheckQuotaTO to = (CheckQuotaTO) serviceTO;
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		String userName = to.getUserName();
		File userDir = new File (storagePath + File.separator + userName);
		
		boolean exceed = true;
		
		if (!userDir.exists()) return null;
			
		storageLength = userDir.length();
		
		calculeStorageLength(userDir.listFiles(), true);
		
		Double storageSize = parseMBToByte(getQuotaSize(userName));
		Double storageExceeded = storageLength - storageSize;
	
		exceed = storageLength >= storageSize;
		
		Double storageUsedPercentage = 1D;
		if (storageLength <= storageSize) {
			storageUsedPercentage = storageLength / storageSize;
		}
		
		String storageExceededFormated = formatSize(storageExceeded);
		
		
		CheckQuotaResponseTO responseTO = new CheckQuotaResponseTO();
		responseTO.setQuotaExed(exceed);
		responseTO.setQuotaExceeded(storageExceededFormated);
		responseTO.setQuotaPercentage(storageUsedPercentage);
		
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

	private Double parseMBToByte(double size){
		return size * BASE_BYTE_MB;
	}
	
	private Double parseByteToMB(Double size){
		return (size / BASE_BYTE_MB);
	}
	
	private Double parseByteTokB(Double size){
		return (size / BASE_BYTE_KB);
	}
	private Double parseByteToGB(Double size){
		return (size / BASE_BYTE_GB);
	}
	
	private String formatSize(Double value) {
		
		if(value > BASE_BYTE_GB){
			return new DecimalFormat("0.00").format(parseByteToGB(value)) + " GB";
		}else if(value > BASE_BYTE_MB){
			return new DecimalFormat("0.00").format(parseByteToMB(value)) + " MB";
		}else if(value > BASE_BYTE_KB){
			return new DecimalFormat("0.00").format(parseByteTokB(value)) + " KB";
		}
		
		return value + " B";
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		CheckQuotaTO to = (CheckQuotaTO) serviceTO;
		
		getLogger().info("Executor Name: Check Quota" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" UserName: " + to.getUserName());
	}
}