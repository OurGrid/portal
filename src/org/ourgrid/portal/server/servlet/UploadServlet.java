package org.ourgrid.portal.server.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.ourgrid.portal.client.OurGridPortalException;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.UploadFileWindow;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.service.ExtractFilesTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.logic.BrokerPortalController;
import org.ourgrid.portal.server.logic.executors.to.response.CheckQuotaExceedWithUploadResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.CheckQuotaExceedWithUploadTO;
import org.ourgrid.portal.server.logic.executors.to.service.DeleteUploadStoreTO;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class UploadServlet extends HttpServlet{
	
	private static final long serialVersionUID = -6524199082110461088L;
	
    @SuppressWarnings("unchecked")
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	if (!ServletFileUpload.isMultipartContent(request)){
            return;
        }

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        String username = request.getParameter(CommonServiceConstants.USER_NAME_PARAMETER);
        String isTempFolder = request.getParameter(CommonServiceConstants.TEMP_FOLDER_PARAMETER);
        String toDescompact = request.getParameter(CommonServiceConstants.TO_DESCOMPACT);
        long uploadId = Long.parseLong(request.getParameter(CommonServiceConstants.UPLOAD_ID_PARAMETER));
        String fileRename = request.getParameter(CommonServiceConstants.FILE_RENAME);
        
        try {
        	
			if (isQuotaExceed(username, request.getContentLength())){
				if(isAUploadToTemporaryFolder(isTempFolder)){
					deleteStore(uploadId);
				}
				response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			
		} catch (Exception e) {
			getLogger().error("Error while uploading file", e);
		}
        
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
        	getLogger().error("Error while uploading file", e);
            return;
        }
        
        for (FileItem fileItem : items) {
            
        	if (fileItem.isFormField()){
                continue;
            }
        	
            String uploadDirName = "";
			
            try {
				uploadDirName = getUploadDirName(uploadId);
			} catch (OurGridPortalException e1) {
				getLogger().error("Error", e1);
			}
			
            File uploadDirFile = new File(uploadDirName);
            uploadDirFile.mkdirs();
            
            String uploadedFileName;
            
            if(fileRename != null) {
            	uploadedFileName = cleanUpUploadedFileRename(fileItem, uploadDirName, fileRename);
            }else{
            	uploadedFileName = cleanUpUploadedFileName(fileItem, uploadDirName);
            }
             
            File uploadedFile = new File(uploadedFileName);
            writeUploadedFile(fileItem, uploadedFile);
            
            String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
            
            if (toDescompact(toDescompact) && isCompactedFile(uploadedFileName)) {
            	try {
            		ServiceTO extractFileTO = createExtractFileTO(uploadDirName, storagePath, uploadedFile, username);
					getPortal().execute(extractFileTO);
            	} catch (Exception e) {
            		getLogger().error("Error while executing an service", e);
            	} 
            }
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }

	private boolean toDescompact(String toDescompact) {
		return new Boolean(toDescompact);
	}

	private boolean isAUploadToTemporaryFolder(String isTempFolder) {
		return new Boolean(isTempFolder);
	}

	private void deleteStore(long uploadId) throws Exception {
		
		DeleteUploadStoreTO to = new DeleteUploadStoreTO();
		to.setExecutorName(CommonServiceConstants.DELETE_UPLOAD_STORE_EXECUTOR);
		to.setUploadId(uploadId);
		
		getPortal().execute(to);
	}

	private boolean isQuotaExceed(String userName, int contentLength) throws Exception {
		
		CheckQuotaExceedWithUploadTO to = new CheckQuotaExceedWithUploadTO();
		to.setExecutorName(CommonServiceConstants.CHECK_QUOTA_EXCEED_WITH_UPLOAD_EXECUTOR);
		to.setUserName(userName);
		to.setFileSize(contentLength);
		
		CheckQuotaExceedWithUploadResponseTO responseTO = 
			(CheckQuotaExceedWithUploadResponseTO) getPortal().execute(to);
		
		return responseTO.isExceed();
	}

	private OurGridPortalIF getPortal() throws OurGridPortalException {
		return BrokerPortalController.getInstance();
	}
	
	private String getUploadDirName(long uploadId) throws OurGridPortalException {
		return getPortal().getDAOManager().getUploadDirName(uploadId);
	}

	private Logger getLogger() {
		Logger logger = null;
		
		 try {
			logger = getPortal().getLogger();
		} catch (OurGridPortalException e) {}
		
		return logger;
	}

	private void writeUploadedFile(FileItem fileItem, File uploadedFile)
			throws ServletException {
		try {
			uploadedFile.createNewFile();
			fileItem.write(uploadedFile);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private String cleanUpUploadedFileName(FileItem item, String uploadFolder) {
		String uploadFile = uploadFolder;
		uploadFile +=  System.getProperty("file.separator");
		uploadFile +=  cleanUpFileName(item.getName());
		return uploadFile;
	}
	
	private String cleanUpUploadedFileRename(FileItem item, String uploadFolder, String fileRename) {
		String uploadFile = uploadFolder;
		uploadFile +=  System.getProperty("file.separator");
		uploadFile +=  cleanUpFileName(fileRename);
		return uploadFile;
	}

	private ServiceTO createExtractFileTO(String uploadDirName, String storagePath, File theFile, String username) {
		
		String location =  (uploadDirName + File.separator).replace(storagePath, "");
		FileTO fileTO = new FileTO(theFile.getName(), location, theFile.isDirectory());
		
		ExtractFilesTO extractFileTO = new ExtractFilesTO();
		extractFileTO.setExecutorName(CommonServiceConstants.EXTRACT_FILES_EXECUTOR);
		extractFileTO.setFileTO(fileTO);
		extractFileTO.setUserName(username);
		
		return extractFileTO;
		
	}

	private String cleanUpFileName(String name) {

		String cleanName = name;
		int slash = name.lastIndexOf("/");
        
		if (slash == -1) {
            slash = name.lastIndexOf("\\");
	    }
		
		if (slash != -1) {
	        cleanName = name.substring(slash + 1);
	    }
	        
	    return cleanName;
	}
	
	private boolean isCompactedFile(String fileName) {
		
		String[] validExtensions = new String[] {"zip", "tar.gz"};
		
		for (String validExtension : validExtensions) {
			if (fileName.endsWith(validExtension)) {
				return true;
			}
		}
		
		return false;
	}
	
}