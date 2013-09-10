package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UploadSessionIDResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UploadSessionIDTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class UploadSessionIDExecutor extends AbstractExecutor {

	private boolean proxyCertificate;
	
	public UploadSessionIDExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		Long uploadSessionId = null;
		
		UploadSessionIDTO uploadSessionIdTO = (UploadSessionIDTO) serviceTO;
		this.setProxyCertificate(uploadSessionIdTO.isProxyCertificate());
		try {
			uploadSessionId = System.nanoTime();
			String uploadDirName = createUploadDirName(uploadSessionId, uploadSessionIdTO.getUserLogin(), uploadSessionIdTO.getLocation());
			getPortal().getDAOManager().createDataStore(uploadSessionId, uploadDirName);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		UploadSessionIDResponseTO responseTO = new UploadSessionIDResponseTO();
		responseTO.setUploadId(uploadSessionId);
		
		return responseTO;
	}
	
	private String createUploadDirName(Long uploadSessionId, String userLogin, String location) {
		
		String uploadFolder = "";
		if(location == null){
			uploadFolder = getUploadDirectory();
			uploadFolder += System.getProperty("file.separator");
			uploadFolder += userLogin;
			uploadFolder += System.getProperty("file.separator");
			File uploadDirFile;
			if(!isProxyCertificate()) {
				uploadFolder += "File Submission [" + longToDate() + "]";
			}
			uploadDirFile = new File(uploadFolder);
			
            uploadDirFile.mkdirs();
		}else{
			uploadFolder = getUploadDirectory();
			uploadFolder += location;
		}
		return uploadFolder;
	}
	
	private String getUploadDirectory() {
		String uploadPath = OurgridPortalProperties.getInstance().
								getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		File uploadDirectory = new File(uploadPath);
		
		if(!uploadDirectory.exists()){
			uploadDirectory.mkdirs();
		}
		return uploadPath;
	}
	
	private String longToDate() {
		Date date = new Date();
	    DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
	    String dateString= dateFormat.format(date);
	    return dateString;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UploadSessionIDTO uploadSessionTO = (UploadSessionIDTO) serviceTO;
		
		getLogger().info("Executor Name: Upload Session Id" + LINE_SEPARATOR +
				"Parameters: " + "User Login: " + uploadSessionTO.getUserLogin());
	}

	public void setProxyCertificate(boolean proxyCertificate) {
		this.proxyCertificate = proxyCertificate;
	}

	public boolean isProxyCertificate() {
		return proxyCertificate;
	}
}