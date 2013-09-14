package org.ourgrid.portal.server.logic;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ourgrid.broker.status.JobStatusInfo;
import org.ourgrid.broker.status.TaskStatusInfo;
import org.ourgrid.common.interfaces.to.GridProcessState;
import org.ourgrid.common.interfaces.to.JobsPackage;
import org.ourgrid.common.util.StringUtil;
import org.ourgrid.portal.client.common.StateConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.JobTO;
import org.ourgrid.portal.client.common.to.model.ProcessTO;
import org.ourgrid.portal.client.common.to.model.ResultTO;
import org.ourgrid.portal.client.common.to.model.TaskTO;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalModel;
import org.ourgrid.portal.server.logic.interfaces.JobStatusUpdateListener;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.logic.util.JobTOFactory;
import org.ourgrid.portal.server.mailer.MailFactory;
import org.ourgrid.portal.server.mailer.Mailer;
import org.ourgrid.portal.server.servlet.DownloadServlet;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

import com.extjs.gxt.ui.client.data.ModelData;

public class JobStatusHandler implements JobStatusUpdateListener {

	private OurGridPortalIF portal;

	//TODO essa classe eh responsavel por executar o algoritmo de atualizacao do status dos jobs
	public JobStatusHandler(OurGridPortalIF portal) {
		this.portal = portal;
	}

	public void hereIsJobDescription(JobsPackage jobPackage){
		Map<Integer, JobStatusInfo> allJobs = jobPackage.getJobs();
		
		if (allJobs == null) {
			return;
		}
		
		for (Integer jobId : allJobs.keySet()){
			JobStatusInfo jobInfo = allJobs.get(jobId);
			
			AbstractRequest request = getPortal().getDAOManager().getRequest(jobId);
			
			if (jobInfo != null && request != null){
				
				JobTO oldJobTO = getJobTO(jobInfo.getJobId());
				JobTO newJobTO = null;
				
				if (oldJobTO == null || isRunning(oldJobTO.getStatus())) {
					newJobTO = JobTOFactory.generateJobTO(jobInfo);
					getBrokerPortalModel().updateJobTO(newJobTO);
					BrokerPortalAsyncApplicationClient client = portal.getBrokerPortalClient();
					client.getPagedTasks(jobId, 0, newJobTO.getNumTasks());
				} else {
					newJobTO = oldJobTO;
				}
				
				if (verifyJobFinished(newJobTO)) {
					if (request.isEmailNotification()) {
						sendJobFinishedEmail(request, newJobTO);
						request.deactivateNotification();
					}
				}
				
				if (isCancelled(newJobTO)) {
					getBrokerPortalModel().removeActiveJobId(newJobTO.getId());
				}
			}
		}
	}

	private boolean isCancelled(JobTO newJobTO) {
		return newJobTO.getStatus().equals(StateConstants.CANCELLED.toString());
	}

	private boolean isRunning(String state) {
		return state.equals(StateConstants.UNSTARTED.toString()) || 
			state.equals(StateConstants.RUNNING.toString());
	}
	
	private JobTO getJobTO(int jobId) {
		return getBrokerPortalModel().getStatus(jobId);
	}
	
	private BrokerPortalModel getBrokerPortalModel() {
		return getPortal().getBrokerPortalClient().getModel();
	}

	public OurGridPortalIF getPortal() {
		return portal;
	}

	private boolean verifyJobFinished(JobTO ourJob) {
		return ourJob.getStatus().equals(StateConstants.FINISHED.toString());
	}
	
	private void sendJobFinishedEmail(AbstractRequest request, JobTO jobTO) {
		User user = getPortal().getDAOManager().getUserByLogin(request.getUserLogin());
		Mailer.send(MailFactory.buildJobFinishMail(user.getEmail(), jobTO.getLabel()));
	}
	
	public void addResults(TaskTO taskTO, Integer jobId) {
		
		ProcessTO finishedProcess  = null;
		for (ModelData processModel : taskTO.getProcesses()) {
			
			ProcessTO processTO = (ProcessTO) processModel;
			
			if (processTO.getStatus().equals(GridProcessState.FINISHED.toString())) {
				finishedProcess = processTO;
				break;
			}
		}
		
		if (finishedProcess == null || finishedProcess.hasResultTO()) return;
		
		LinkedList<File> resultFiles = new LinkedList<File>();
		Map<String, String> envVars = new HashMap<String, String>();
		
		envVars.put( "PROC", finishedProcess.getAssignedTo());
		envVars.put( "TASK", taskTO.getId().toString());
		envVars.put( "JOB", jobId.toString());
		
		for (String resultPath : taskTO.getResults()) {
			resultPath = StringUtil.replaceVariables(resultPath, envVars);
			resultFiles.add(new File(resultPath));
		}
		
		AbstractRequest request = getPortal().getDAOManager().getRequest(jobId);
		List<ResultTO> results = createResults(resultFiles, request.getUploadId());
		finishedProcess.setResultTO(results);
	}
	
	private List<ResultTO> createResults(List<File> resultFiles, long uploadId) {
		
		List<ResultTO> results = new LinkedList<ResultTO>();
		
		for (File file : resultFiles){

			ResultTO resultTO = new ResultTO();
			resultTO.setId(resultTO.toString());
			resultTO.setText(file.getName());
			resultTO.setUrl("download?" + DownloadServlet.FILE_NAME_PARAMETER + "=" + file.getName() + "&" + DownloadServlet.FILE_LOCATION +"=" + 
					parseLocation(file.getParentFile().getAbsolutePath()));
			resultTO.setName(resultTO.toString());
			
			results.add(resultTO);
		}
		return results;
	}

	private String parseLocation(String absolutePath) {
		File storageDir = new File(OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY));
		
		return absolutePath.replace(storageDir.getAbsolutePath(), "");
	}

	public void hereIsPagedTasks(Integer offset, List<TaskStatusInfo> pagedTasks) {
		
		List<ModelData> tasks = new LinkedList<ModelData>();
		
		Integer jobId = 0;
		
		for (TaskStatusInfo taskStatusInfo : pagedTasks) {
			TaskTO taskTO = JobTOFactory.generateTaskTO(taskStatusInfo);
			jobId = taskTO.getJobId();
			if (taskTO.getStatus().equals(GridProcessState.FINISHED.toString())) {
				addResults(taskTO, jobId);
			}
			tasks.add(taskTO);
		}
		
		getBrokerPortalModel().updateJobTasksTO(jobId, offset, tasks);
	}
	
}