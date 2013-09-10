package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.FileExplorerResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.FileExplorerTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.CheckQuotaResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.CheckQuotaTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class FileExplorerExecutor extends AbstractExecutor {

	private static final long BASE_BYTE_MB = 1048576;
	private static final long BASE_BYTE_KB = 1024;
	private static final long BASE_BYTE_GB = 1073741824;
	
	private static final String QUOTA_VERIFICATION_ERROR = "Quota verification error";
	
	private long storageLength;

	public FileExplorerExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		FileExplorerTO fileExplorerTO = (FileExplorerTO) serviceTO;
		FileExplorerResponseTO fileExplorerResponseTO = new FileExplorerResponseTO();
		
		String nodeName = fileExplorerTO.getNodeName();
		String location = fileExplorerTO.getLocation();
		String userName = fileExplorerTO.getUserName();
		boolean checkQuota = fileExplorerTO.checkQuota();
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		File file = new File (storagePath + location);
		
		FileTO root = createFilesExplorer(nodeName, file.lastModified(), file.listFiles(), location, true);
		
		if(checkQuota){
			
			CheckQuotaResponseTO checkQuotaResponseTO = null;
			try {
				checkQuotaResponseTO = verifyQuota(userName);
			} catch (Exception e) {
			}
			
			if(checkQuotaResponseTO == null){
				throw new ExecutionException(QUOTA_VERIFICATION_ERROR);
			}
			
			User uLogin = getPortal().getDAOManager().getUserByLogin(userName);
			List<AbstractRequest> jobs = getPortal().getDAOManager().getJobsRequestByLogin(uLogin.getLogin());
			
			if (null == jobs){
				jobs = new LinkedList<AbstractRequest>();
			}
			
			fileExplorerResponseTO.setQuotaExed(checkQuotaResponseTO.getQuotaExed());
			fileExplorerResponseTO.setQuotaUsed(checkQuotaResponseTO.getQuotaUsed());
			fileExplorerResponseTO.setQuotaExceeded(checkQuotaResponseTO.getQuotaExceeded());
			fileExplorerResponseTO.setQuotaPercentage(checkQuotaResponseTO.getQuotaPercentage());
			fileExplorerResponseTO.setJobsRequestList(jobs);
		}
		
		fileExplorerResponseTO.setFileRoot(root);
		fileExplorerResponseTO.setCheckQuota(checkQuota);
		
		return fileExplorerResponseTO;
	}
	
	private CheckQuotaResponseTO verifyQuota(String userName) throws Exception {
		
		CheckQuotaTO to = new CheckQuotaTO();
		to.setExecutorName(CommonServiceConstants.CHECK_QUOTA_EXECUTOR);
		to.setUserName(userName);
		
		CheckQuotaResponseTO responseTO = 
			(CheckQuotaResponseTO) getPortal().execute(to);
		
		return responseTO;
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
	
	private FileTO createFilesExplorer(String name, Long date, File[] list, String location, boolean isFolder) {
		
		List<FileTO> listFolders = new ArrayList<FileTO>();
		List<FileTO> listFiles = new ArrayList<FileTO>();
		
		boolean hasChildren = false;
		
		if(list != null){
			for (int i = 0; i < list.length; i++) {
				
				if(!list[i].isHidden()){
					
					FileTO fileTO;
					if(list[i].isDirectory()){
						hasChildren = hasChildren || true;
						fileTO = new FileTO(list[i].getName(), location + list[i].getName() + File.separator, true);
						fileTO.setDate(new Date(list[i].lastModified()));
						fileTO.setHasChildren(hasChildren(list[i]));
						listFolders.add(fileTO);
					}else{
						String size = formatSize(new Double(list[i].length()));
						fileTO = new FileTO(list[i].getName(), size, new Date(list[i].lastModified()), location, false);
						storageLength += list[i].length();
						listFiles.add(fileTO);
					}
				}
			}
		}
		
		FileTO file = new FileTO(name, listFolders, location, isFolder);
		file.setListFiles(listFiles);
		file.setDate(new Date(date));
		
		file.setHasChildren(hasChildren);
		return file;
	}

	private boolean hasChildren(File file) {
		
		for (File child : file.listFiles()) {
			if(child.isDirectory()) return true;
		}
		return false;
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
		
		FileExplorerTO fileExplorerTO = (FileExplorerTO) serviceTO;
		
		getLogger().info("Executor Name: File Explorer" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" Storage Path: " + fileExplorerTO.getLocation() + LINE_SEPARATOR);
	}
}