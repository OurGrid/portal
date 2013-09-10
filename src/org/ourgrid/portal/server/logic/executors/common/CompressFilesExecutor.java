package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.CompressFilesResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.CompressFilesTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.CheckQuotaResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.CheckQuotaTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.util.CompressFiles;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class CompressFilesExecutor extends AbstractExecutor{

	private static final long BASE_BYTE_MB = 1048576;
	private static final long BASE_BYTE_KB = 1024;
	private static final long BASE_BYTE_GB = 1073741824;
	
	private static final String SUCCESSUFULLY_COMPACTED = "The file was successfully compacted.";
	private static final String EMPTY_DIRECTORY = "The directory is empty";
	private static final String QUOTA_VERIFICATION_ERROR = "Quota verification error";
	
	public CompressFilesExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		CompressFilesTO compactFilesTO = (CompressFilesTO) serviceTO;
		List<FileTO> extractedFilesTO = compactFilesTO.getFile();
		String userName = compactFilesTO.getUserName();
		CompressFilesResponseTO responseTO = new CompressFilesResponseTO();
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		
		for (FileTO extractedFileTO : extractedFilesTO) {
			File extractedFile = new File(storagePath + extractedFileTO.getLocation());
			CompressFiles compactor = new CompressFiles(extractedFile);
			File file;
			try {
				file = compactor.pack();
			} catch (IOException e) {
				getLogger().error("Error while packing file", e);
				throw new ExecutionException("Error while packing file");
			}
			
			if(file == null){
				throw new ExecutionException(EMPTY_DIRECTORY);
			}
				
			String newDate = DateFormat.getInstance().format(new Date(file.lastModified()));
			String size = formatSize(new Double(file.length()));
			FileTO fileTO = new FileTO(file.getName(), size, newDate, ((FileTO)extractedFileTO.getParent()).getLocation(), false);
			
			CheckQuotaResponseTO checkQuotaResponseTO = null;
			try {
				checkQuotaResponseTO = verifyQuota(userName);
			} catch (Exception e) {
			}
			
			responseTO = new CompressFilesResponseTO();
			responseTO.setMessage(SUCCESSUFULLY_COMPACTED);
			responseTO.setFile(fileTO);
			
			if(checkQuotaResponseTO == null){
				throw new ExecutionException(QUOTA_VERIFICATION_ERROR);
			}
			
			User uLogin = getPortal().getDAOManager().getUserByLogin(userName);
			List<AbstractRequest> jobs = getPortal().getDAOManager().getJobsRequestByLogin(uLogin.getLogin());
			
			if (null == jobs){
				jobs = new LinkedList<AbstractRequest>();
			}
			
			responseTO.setQuotaExed(checkQuotaResponseTO.getQuotaExed());
			responseTO.setQuotaUsed(checkQuotaResponseTO.getQuotaUsed());
			responseTO.setQuotaPercentage(checkQuotaResponseTO.getQuotaPercentage());
			responseTO.setQuotaExceeded(checkQuotaResponseTO.getQuotaExceeded());
			responseTO.setJobsRequestList(jobs);
			responseTO.setMessage(SUCCESSUFULLY_COMPACTED);
		}
		
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
	
	private Double parseByteToMB(Double size){
		return (size / BASE_BYTE_MB);
	}
	
	private Double parseByteTokB(Double size){
		return (size / BASE_BYTE_KB);
	}
	private Double parseByteToGB(Double size){
		return (size / BASE_BYTE_GB);
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		CompressFilesTO compressFilesTO = (CompressFilesTO) serviceTO;
		for (FileTO fileTO : compressFilesTO.getFile()) {
			getLogger().info("Executor Name: Compact Files" + LINE_SEPARATOR + 
					"Parameters: "  + LINE_SEPARATOR + 
					" FileName: " + fileTO.getName());
		}
		
	}
}