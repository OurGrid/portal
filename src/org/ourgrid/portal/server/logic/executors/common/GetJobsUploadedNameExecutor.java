package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.to.response.GetJobsUploadedNameResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.GetJobsUploadedNameTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.OurGridPortalConstants;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.util.FileUtil;

public class GetJobsUploadedNameExecutor extends AbstractExecutor {

	public GetJobsUploadedNameExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		GetJobsUploadedNameTO getJobsUploadedNameTO = (GetJobsUploadedNameTO) serviceTO;

		String rootDir =
			getPortal().getDAOManager().getUploadDirName(getJobsUploadedNameTO.getUploadSessionId());
		
		List<File> jobFiles = FileUtil.filterFiles(rootDir, OurGridPortalConstants.JDF_EXTENSION);

		List<String> jobFilesName = new LinkedList<String>();
		for (File file : jobFiles) {
			jobFilesName.add(file.getName());
		}
		GetJobsUploadedNameResponseTO responseTO = new GetJobsUploadedNameResponseTO();
		responseTO.setJdfNames(jobFilesName);
		
		return responseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		GetJobsUploadedNameTO getJobsUploadedNameTO = (GetJobsUploadedNameTO) serviceTO;
		
		getLogger().info("Executor Name: Get Jobs Uploaded" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" Upload Session Id: " + getJobsUploadedNameTO.getUploadSessionId());
	}
}