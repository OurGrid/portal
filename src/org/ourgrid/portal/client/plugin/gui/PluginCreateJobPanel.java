package org.ourgrid.portal.client.plugin.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.DeleteJobTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class PluginCreateJobPanel extends VerticalPanel {
	
	protected TabPanel tabJob;
	protected Integer jobTabCounter;
	protected PluginJobSubmissionPanel jobSubmissionPanel;

	public PluginCreateJobPanel() {
		this.jobTabCounter = 0;
		init();
	}
	
	private void init() {
		this.setSize(800, 650);
		this.setSpacing(0);
		this.setBorders(false);
		createTabJob();
	}

	protected void createTabJob() {
		tabJob = new TabPanel();
		tabJob.setSize(765, 540);
		this.add(tabJob);
	}

	protected PluginJobSubmissionPanel createJobSubmitPanel(int jobViewId, TabItem container) {
		return jobSubmissionPanel;
		
	}

	public abstract TabItem addTab(AbstractRequest request);

	public TabPanel getTabPanel() {
		return tabJob;
	}

	public TabItem addTabJob(int jobViewId, boolean newJob, List<?> inputs) {
		final TabItem item = new TabItem();
		item.setLayout(new FitLayout());

		final PluginJobSubmissionPanel jobSubmitPanel = createJobSubmitPanel(jobViewId, item);
		
		item.add(jobSubmitPanel);
		item.setText("Job " + ++jobTabCounter);
		
	    item.addListener(Events.Show, new Listener<BaseEvent>() {

			public void handleEvent(BaseEvent be) {
				PluginJobSubmissionPanel jobSubmissionPanel = (PluginJobSubmissionPanel) item.getWidget(0);
				jobSubmissionPanel.refresh();
			}
	    });
	    
	    item.addListener(Events.BeforeClose, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				PluginJobSubmissionPanel jobSubmissionPanel = (PluginJobSubmissionPanel) item.getWidget(0);
				handleTabClosed(jobSubmissionPanel);
			}
  		});

		item.setClosable(true);
		this.tabJob.add(item);
		if(!newJob) {
			jobSubmitPanel.processInputs(inputs);
		}

		return item;
	}
	
	protected void handleTabClosed(PluginJobSubmissionPanel statusPanel) {
  		
  		statusPanel.stopGetStatusAction();
  		deleteJob(statusPanel.getJobId());
  		
  		
		// this is made before the tab is closed, so if there's one tab, the job status is invisible
  		if (tabJob.getItems().size() == 1) {
  			TabItem tab = addTabJob();
  			tab.setClosable(true);
  		}
	}
	
	private void deleteJob(Integer jobId) {
		DeleteJobTO deleteJobTO = new DeleteJobTO();
  		deleteJobTO.setExecutorName(CommonServiceConstants.DELETE_JOB_EXECUTOR);
  		deleteJobTO.setJobId(jobId);
  		
		OurGridPortalServerUtil.getInstance().execute(deleteJobTO, new AsyncCallback<ResponseTO>() {

			public void onSuccess(ResponseTO result) {
				
			}
			
			public void onFailure(Throwable caught) {
				
			}
			
		});
	}
	
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<Object>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}
}
