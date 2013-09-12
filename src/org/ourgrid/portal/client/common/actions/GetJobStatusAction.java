package org.ourgrid.portal.client.common.actions;

import java.util.ArrayList;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.JobStatusPanel;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.response.JobStatusRetrievalResponseTO;
import org.ourgrid.portal.client.common.to.service.JobStatusRetrievalTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetJobStatusAction {
	
	private static final int UPDATE_INTERVAL = 10;

	private Integer jobId;
	private JobStatusPanel jobStatusPanel;
	
	public GetJobStatusAction(Integer jobId, JobStatusPanel panel) {
		this.jobId = jobId;
		this.jobStatusPanel = panel;
	}
	
	public Timer scheduleUpdateTimer() {
		Timer timer = createUpdateTask();
		timer.scheduleRepeating(UPDATE_INTERVAL * 1000);
		return timer;
	}
	
	private Timer createUpdateTask() {
		return new Timer() {
			
			public void run() {
				runAction();
			}
			
		};
	}
	
	public void runAction() {
		
		JobStatusRetrievalTO jobStatusTO = new JobStatusRetrievalTO();
		jobStatusTO.setExecutorName(CommonServiceConstants.JOB_STATUS_RETRIEVAL_EXECUTOR);
		jobStatusTO.setJobId(jobId);
		
		UserModel userModel = OurGridPortal.getUserModel();
		jobStatusTO.setPagesFirstTaskIds(new ArrayList<Integer>(userModel.getPagesFirstTaskIds(jobId)));
		
		OurGridPortalServerUtil.getInstance().execute(jobStatusTO, new AsyncCallback<JobStatusRetrievalResponseTO>() {

			public void onFailure(Throwable caught) {}

			public void onSuccess(JobStatusRetrievalResponseTO result) {
				jobStatusPanel.updateJobStatus(result.getJobStatus());
			}
			
		});
	}
}