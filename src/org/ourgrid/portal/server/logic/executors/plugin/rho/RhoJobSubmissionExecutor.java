package org.ourgrid.portal.server.logic.executors.plugin.rho;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ourgrid.common.specification.exception.JobSpecificationException;
import org.ourgrid.common.specification.exception.TaskSpecificationException;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.rho.gui.model.RhoJobRequest;
import org.ourgrid.portal.client.plugin.rho.to.response.RhoJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.rho.to.service.RhoJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class RhoJobSubmissionExecutor extends JobSubmissionExecutor {
	
	private Integer start = 0;
	private Integer end	  = 1;
	private Integer step  = 2;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private String exeFileName;
	
	public RhoJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		RhoJobSubmissionTO rhoJobSubmissionTO = (RhoJobSubmissionTO) serviceTO;
		RhoJobSubmissionResponseTO rhoJobSubmissionResponseTO = new RhoJobSubmissionResponseTO();
		
		Integer parameterOneStart    = rhoJobSubmissionTO.getParameterOneStart();
		Integer parameterTwoStart    = rhoJobSubmissionTO.getParameterTwoStart();
		Integer parameterThreeStart  = rhoJobSubmissionTO.getParameterThreeStart();
		Integer parameterFourStart 	 = rhoJobSubmissionTO.getParameterFourStart();
		Integer parameterFiveStart 	 = rhoJobSubmissionTO.getParameterFiveStart();
		Integer parameterSixStart 	 = rhoJobSubmissionTO.getParameterSixStart();
		Integer parameterSevenStart  = rhoJobSubmissionTO.getParameterSevenStart();
		
		Integer parameterOneEnd 	 = rhoJobSubmissionTO.getParameterOneEnd();
		Integer parameterTwoEnd 	 = rhoJobSubmissionTO.getParameterTwoEnd();
		Integer parameterThreeEnd 	 = rhoJobSubmissionTO.getParameterThreeEnd();
		Integer parameterFourEnd 	 = rhoJobSubmissionTO.getParameterFourEnd();
		Integer parameterFiveEnd 	 = rhoJobSubmissionTO.getParameterFiveEnd();
		Integer parameterSixEnd 	 = rhoJobSubmissionTO.getParameterSixEnd();
		Integer parameterSevenEnd    = rhoJobSubmissionTO.getParameterSevenEnd();
		
		Integer parameterOneStep	 = rhoJobSubmissionTO.getParameterOneStep();
		Integer parameterTwoStep 	 = rhoJobSubmissionTO.getParameterTwoStep();
		Integer parameterThreeStep 	 = rhoJobSubmissionTO.getParameterThreeStep();
		Integer parameterFourStep	 = rhoJobSubmissionTO.getParameterFourStep();
		Integer parameterFiveStep 	 = rhoJobSubmissionTO.getParameterFiveStep();
		Integer parameterSixStep 	 = rhoJobSubmissionTO.getParameterSixStep();
		Integer parameterSevenStep 	 = rhoJobSubmissionTO.getParameterSevenStep();
		
		List<Integer> parameterOne 	 = new ArrayList<Integer>();
		List<Integer> parameterTwo	 = new ArrayList<Integer>();
		List<Integer> parameterThree = new ArrayList<Integer>();
		List<Integer> parameterFour	 = new ArrayList<Integer>();
		List<Integer> parameterFive  = new ArrayList<Integer>();
		List<Integer> parameterSix 	 = new ArrayList<Integer>();
		List<Integer> parameterSeven = new ArrayList<Integer>();
				
		Map<String,List<Integer>> inputMap = new HashMap<String, List<Integer>>();
		
		
		parameterOne.add(parameterOneStart);
		parameterOne.add(parameterOneEnd);
		parameterOne.add(parameterOneStep);
		inputMap.put("parameterOne",parameterOne);
		
		parameterTwo.add(parameterTwoStart);
		parameterTwo.add(parameterTwoEnd);
		parameterTwo.add(parameterTwoStep);
		inputMap.put("parameterTwo",parameterTwo);
		
		parameterThree.add(parameterThreeStart);
		parameterThree.add(parameterThreeEnd);
		parameterThree.add(parameterThreeStep);
		inputMap.put("parameterThree",parameterThree);
		
		parameterFour.add(parameterFourStart);
		parameterFour.add(parameterFourEnd);
		parameterFour.add(parameterFourStep);
		inputMap.put("parameterFour",parameterFour);
		
		parameterFive.add(parameterFiveStart);
		parameterFive.add(parameterFiveEnd);
		parameterFive.add(parameterFiveStep);
		inputMap.put("parameterFive",parameterFive);
		
		parameterSix.add(parameterSixStart);
		parameterSix.add(parameterSixEnd);
		parameterSix.add(parameterSixStep);
		inputMap.put("parameterSix",parameterSix);
		
		parameterSeven.add(parameterSevenStart);
		parameterSeven.add(parameterSevenEnd);
		parameterSeven.add(parameterSevenStep);
		inputMap.put("parameterSeven",parameterSeven);
		
		
		String rhoPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.RHO_PATH);
		
		initializeClient();
		
		exeFileName = rhoPath;
		File file = new File (exeFileName);
		try {
			exeFileName = file.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		String userLogin = rhoJobSubmissionTO.getUserLogin();
		boolean emailNotification = rhoJobSubmissionTO.isEmailNotification();
		
		String outputDir = createOutputDir(userLogin);
		
		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(inputMap, outputDir);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = addJob(rhoJobSubmissionTO.getUserLogin(), theJob);
		rhoJobSubmissionResponseTO.getJobIDs().add(jobId);
		addRequest(jobId, new RhoJobRequest(jobId ,0, userLogin, emailNotification));
		reeschedule();
		
		return rhoJobSubmissionResponseTO;
	}

	private JobSpecification compileInputs(Map<String,List<Integer>> inputList, String outputDir) throws JobSpecificationException, TaskSpecificationException, IOException {
		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();
		
		for (int a = getStart(inputList,"parameterOne"); a <= getEnd(inputList,"parameterOne"); a+= getStep(inputList,"parameterOne")) {
			for (int b = getStart(inputList,"parameterTwo"); b <= getEnd(inputList,"parameterTwo"); b+= getStep(inputList,"parameterTwo")) {
				for (int c = getStart(inputList,"parameterThree"); c <= getEnd(inputList,"parameterThree"); c+= getStep(inputList,"parameterThree")) {
					for (int d = getStart(inputList,"parameterFour"); d <= getEnd(inputList,"parameterFour"); d+= getStep(inputList,"parameterFour")) {
						for (int e = getStart(inputList,"parameterFive"); e <= getEnd(inputList,"parameterFive"); e+= getStep(inputList,"parameterFive")) {
							for (int f = getStart(inputList,"parameterSix"); f <= getEnd(inputList,"parameterSix"); f+= getStep(inputList,"parameterSix")) {
								for (int g = getStart(inputList,"parameterSeven"); g <= getEnd(inputList,"parameterSeven"); g+= getStep(inputList,"parameterSeven")) {

									// create initBlock
									IOBlock initBlock = new IOBlock();
									IOEntry initEntry = new IOEntry("put", exeFileName, "rho");
									initBlock.putEntry(initEntry);
									
									String remoteExe = "/bin/echo " + a + " " + b + " " + c + " " + d + " " + e + " " + f + " " + g + " > in.txt;/bin/chmod +x rho;./rho;/bin/tar -cf $JOB-wu$TASK.tar *;"+ LINE_SEPARATOR;
									
									//create finalBlock
									IOBlock finalBlock = new IOBlock();
									IOEntry finalEntry = new IOEntry("get","$JOB-wu$TASK.tar", outputDir + File.separator + "$JOB-wu$TASK.tar" );
									finalBlock.putEntry(finalEntry);
									
									TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
									tasksList.add(taskSpec);
									
								}
							}
						}
					}
				}
			}
		}
		
		return new JobSpecification("Rho Job", "", tasksList);
	}
	
	private String createOutputDir(String userLogin) {
			
		String outputDir = "";

		outputDir  = getUploadDirectory();
		outputDir += System.getProperty("file.separator");
		outputDir += userLogin;
		outputDir += System.getProperty("file.separator");
		outputDir += "SLinCA Job Submission [" + longToDate() + "]";

		File uploadDirFile = new File(outputDir);
		uploadDirFile.mkdirs();

		return outputDir;
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
	    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
	    String dateString= dateFormat.format(date);
	    return dateString;
	}

	private Integer getStart(Map<String,List<Integer>> inputList, String parameter) {
		return inputList.get(parameter).get(start);
	}
	
	private Integer getEnd(Map<String,List<Integer>> inputList, String parameter) {
		return inputList.get(parameter).get(end);
	}
	
	private Integer getStep(Map<String,List<Integer>> inputList, String parameter) {
		return inputList.get(parameter).get(step);
	}

	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub
		
	}
	
	
}