package org.ourgrid.portal.client.common.util;

import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.service.CompressFilesTO;
import org.ourgrid.portal.client.common.to.service.DeleteFileTO;
import org.ourgrid.portal.client.common.to.service.ExtractFilesTO;
import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.common.to.service.NewFolderTO;
import org.ourgrid.portal.client.common.to.service.PasteFileTO;
import org.ourgrid.portal.client.common.to.service.RenameFileTO;

public class FileExplorerUtil {
	
	public static PasteFileTO createPasteFileTO(FileTO rootNode, FileTO source, List<FileTO> cutFileList, List<FileTO> copyFileList, String userName) {
		
		PasteFileTO pasteFileTO = new PasteFileTO();
		pasteFileTO.setExecutorName(CommonServiceConstants.PASTE_FILE_EXECUTOR);
		pasteFileTO.setRoot(rootNode);
		pasteFileTO.setUserName(userName);
		
		if(!cutFileList.isEmpty()){
			pasteFileTO.setCopy(false);
			pasteFileTO.setListFiles(cutFileList);
			pasteFileTO.setSource(source);
		}else if(!copyFileList.isEmpty()){
			pasteFileTO.setCopy(true);
			pasteFileTO.setListFiles(copyFileList);
		}
		return pasteFileTO;
	}

	public static CompressFilesTO createCompressFileTO(List<FileTO> fileTO, String userName) {
		
		CompressFilesTO compressFileTO = new CompressFilesTO();
		compressFileTO.setExecutorName(CommonServiceConstants.COMPRESS_FILES_EXECUTOR);
		compressFileTO.setFile(fileTO);
		compressFileTO.setUserName(userName);
		return compressFileTO;
		
	}

	public static RenameFileTO createRenameFileTO(FileTO fileTO, String name) {
		
		RenameFileTO renameFileTO = new RenameFileTO();
		renameFileTO.setExecutorName(CommonServiceConstants.RENAME_FILE_EXECUTOR);
		renameFileTO.setName(name);
		renameFileTO.setFile(fileTO);
		return renameFileTO;
	}
	
	public static NewFolderTO createNewFolderTO(String name, FileTO rootNode) {
		
		NewFolderTO newFolderTO = new NewFolderTO();
		newFolderTO.setExecutorName(CommonServiceConstants.NEW_FOLDER_EXECUTOR);
		newFolderTO.setFile(name);
		newFolderTO.setRoot(rootNode);
		return newFolderTO;
	}
	
	public static DeleteFileTO createDeleteFileTO(List<FileTO> listFile, FileTO rootNode, String userName) {
		
		DeleteFileTO deleteFileTO = new DeleteFileTO();
		deleteFileTO.setExecutorName(CommonServiceConstants.DELETE_FILE_EXECUTOR);
		deleteFileTO.setListFiles(listFile);
		deleteFileTO.setRoot(rootNode);
		deleteFileTO.setUserName(userName);
		return deleteFileTO;
	}
	
	public static JobSubmissionTO createJobSubmissionTO(Long uploadSessionId, String userLogin,
			String jobName, boolean isEmailNotification, String location) {
		
		JobSubmissionTO jobSubmissionTO = new JobSubmissionTO();
		jobSubmissionTO.setExecutorName(CommonServiceConstants.JOB_SUBMISSION_EXECUTOR);
		jobSubmissionTO.setUserLogin(userLogin);
		jobSubmissionTO.setJobName(jobName);
		jobSubmissionTO.setUploadId(uploadSessionId);
		jobSubmissionTO.setLocation(location);
		jobSubmissionTO.setEmailNotification(isEmailNotification);
		return jobSubmissionTO;
	}
	
	public static ExtractFilesTO createExtractFileTO(FileTO fileTO, String userName) {
		
		ExtractFilesTO extractFileTO = new ExtractFilesTO();
		extractFileTO.setExecutorName(CommonServiceConstants.EXTRACT_FILES_EXECUTOR);
		extractFileTO.setFileTO(fileTO);
		extractFileTO.setUserName(userName);
		
		return extractFileTO;
		
	}

}
