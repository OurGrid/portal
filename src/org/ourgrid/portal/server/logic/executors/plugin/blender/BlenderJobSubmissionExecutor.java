package org.ourgrid.portal.server.logic.executors.plugin.blender;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.ourgrid.common.specification.exception.JobSpecificationException;
import org.ourgrid.common.specification.exception.TaskSpecificationException;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.blender.gui.model.BlenderJobRequest;
import org.ourgrid.portal.client.plugin.blender.to.model.BlenderInputFileTO;
import org.ourgrid.portal.client.plugin.blender.to.response.BlenderJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.blender.to.service.BlenderJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

public class BlenderJobSubmissionExecutor extends JobSubmissionExecutor {

	protected Long uploadId;
	
	public BlenderJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		BlenderJobSubmissionTO blenderJobSubmissionTO = (BlenderJobSubmissionTO) serviceTO;
		BlenderJobSubmissionResponseTO blenderJobSubmissionResponseTO = new BlenderJobSubmissionResponseTO();
		
		initializeClient();
		
		uploadId = blenderJobSubmissionTO.getUploadId();
		String userLogin = blenderJobSubmissionTO.getUserLogin();
		boolean emailNotification = blenderJobSubmissionTO.isEmailNotification();
		List<BlenderInputFileTO> inputFileList = blenderJobSubmissionTO.getInputFileList();
		
		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(inputFileList);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		
		Integer jobId = addJob(blenderJobSubmissionTO.getUserLogin(), theJob);
		
		blenderJobSubmissionResponseTO.setJobID(jobId);
		blenderJobSubmissionResponseTO.getJobIDs().add(jobId);
		
		addRequest(jobId, new BlenderJobRequest(jobId, uploadId, userLogin, emailNotification, inputFileList));
		reeschedule();
		
		return blenderJobSubmissionResponseTO;
	}
	
	private JobSpecification compileInputs(List<BlenderInputFileTO> inputFileList) throws JobSpecificationException, TaskSpecificationException, IOException {
		
		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();
		
		for (BlenderInputFileTO inputFileTO : inputFileList) {
			
			Integer scenesNumber = inputFileTO.getScenesNumber();
			String outputTypeFile = inputFileTO.getOutputType();
			String outputExtFile = inputFileTO.getOutputExt();
			String inputFileName = extractFileNameFromCompletePath(inputFileTO.getName());
			Integer startFrame = inputFileTO.getStart();
			Integer endFrame = inputFileTO.getEnd();
			Integer totalFrame = endFrame - startFrame + 1;
			
			int numTasks = totalFrame / scenesNumber;
			numTasks = ((numTasks*scenesNumber) < totalFrame) ? numTasks + 1 : numTasks;
			
			int start = startFrame;
			
			for (int i = 1; i <= numTasks; i++) {
				
				// create initBlock
				IOBlock initBlock = new IOBlock();
				IOEntry initEntry = new IOEntry("put", inputFileName, "my.blend");
				initBlock.putEntry(initEntry);
				
				int end = start + scenesNumber - 1;
				end = (end > endFrame) ? endFrame : end;
				
				//create remotExe
				String remoteExe = "blender -b my.blend -o $PLAYPEN/ -F " + outputTypeFile + " -s " + start + " -e " + end + " -a";
				
				//create finalBlock
				IOBlock finalBlock = new IOBlock();
				
				if (outputTypeFile.equals("AVIJPEG") || outputTypeFile.equals("AVICODEC") || outputTypeFile.equals("AVIRAW") || outputTypeFile.equals("MPEG")){

					String outputName = createOutputName(outputTypeFile, start, end, outputExtFile);
					IOEntry finalEntry = new IOEntry("get", outputName, getFileName(inputFileName) + "_" + outputName);
					finalBlock.putEntry(finalEntry);
				} else {
					
					for (int j = start; j <= end; j++) {
						
						String outputName = createOutputName(outputTypeFile, j, j, outputExtFile);
						IOEntry finalEntry = new IOEntry("get", outputName, getFileName(inputFileName) + "_" + outputName);
						finalBlock.putEntry(finalEntry);
					}
				}
				
				TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
				tasksList.add(taskSpec);
				
				start += scenesNumber;
			}
		}
		return new JobSpecification("Blender Job", "", tasksList);
	}

	private String extractFileNameFromCompletePath(String name) {
		
		File file = new File(name);
		return getPortal().getDAOManager().getUploadDirName(uploadId) + File.separator + file.getName();
	}

	private String createOutputName(String outputTypeFile, int numStart, int numEnd, String ext) {
	
		if( outputTypeFile.equals("AVIJPEG") || outputTypeFile.equals("AVICODEC") || outputTypeFile.equals("AVIRAW") || outputTypeFile.equals("MPEG")){
			return createNameForAVI(numStart,numEnd, ext);
		}
		return createName(numStart, ext);
	}
	
	private String createNameForAVI(int numStart, int numEnd, String ext){
		return new DecimalFormat("0000").format(numStart) + "_" + new DecimalFormat("0000").format(numEnd) + "." + ext ;
	}
	
	private String createName(int num, String ext){
		return new DecimalFormat("0000").format(num) + "." + ext ;
	}
	
	private String getFileName(String inputFileName){
		return inputFileName.substring(0,inputFileName.lastIndexOf(".")).toLowerCase();
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		BlenderJobSubmissionTO blenderJobSubmissionTO = (BlenderJobSubmissionTO) serviceTO;
		
		getLogger().info("Executor Name: Genecodis Job Submission" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + blenderJobSubmissionTO.getUserLogin() + LINE_SEPARATOR + 
				" UploadId: " + blenderJobSubmissionTO.getUploadId());
	}
}