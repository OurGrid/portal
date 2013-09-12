package org.ourgrid.portal.client.plugin.gui;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.JobStatusPanel;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.response.JobSubmissionResponseTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public abstract class PluginJobSubmissionPanel extends LayoutContainer {

	private TabItem tabItem;
	private Integer jobTabCount;
	private Integer jobViewId;
	
	protected final int SUBMIT_FIELDSET_WIDTH = 740;

	protected VerticalPanel mainPanel;
	protected FieldSet jobStatusFieldSet;
	protected JobStatusPanel jobStatusPanel;

	public PluginJobSubmissionPanel(Integer jobTabCounter, int jobViewId,
			TabItem container) {
		super();

		this.setTabItem(container);
		this.setJobTabCount(jobTabCounter);
		this.setJobViewId(jobViewId);
		
		init();

		this.setAutoWidth(true);
		this.setAutoHeight(true);
		this.add(this.mainPanel);
		

	}

	protected abstract void init();
	/**
	 * Method that creates the main panel
	 */
	protected void createMainPanel() {
		this.mainPanel = new VerticalPanel();  
		this.mainPanel.setSpacing(5);
		this.mainPanel.setScrollMode(getScrollMode());
	}

	/**
	 * Method that create the status fieldset
	 */
	protected void createStatusFieldSet(String message) {
		this.jobStatusFieldSet = new FieldSet();
		this.jobStatusFieldSet.setHeading(message);  
		this.jobStatusFieldSet.setWidth(SUBMIT_FIELDSET_WIDTH);
		this.jobStatusFieldSet.setBorders(true);
		this.jobStatusFieldSet.setVisible(false);

		FormLayout layout = new FormLayout(); 
		layout.setLabelWidth(200);

		this.jobStatusFieldSet.setLayout(layout);
		this.mainPanel.add(this.jobStatusFieldSet);
	}

	/**
	 * Method that creates the status panel
	 */
	protected void createStatusPanel(JobStatusPanel jobStatusPanel) {
		this.jobStatusPanel = jobStatusPanel;
		this.jobStatusPanel.setWidth(710);
		this.jobStatusPanel.setVisible(false);

		this.jobStatusFieldSet.add(this.jobStatusPanel);
	}

	protected void refresh() {
		doLayout();		
	}

	protected void stopGetStatusAction() {
		this.jobStatusPanel.stopStatusTimer();
	}

	protected Integer getJobId() {
		return this.jobStatusPanel.getJobId();
	}

	protected void closeWindow() {
		this.setVisible(false);
	}

	protected abstract void desactivateSubmission();

	protected abstract void activateSubmission();

	/**
	 * Method that process the result of the execution
	 * @param result the result of the execution
	 */
	protected void processJobSubmissionResponse(JobSubmissionResponseTO result) {
		List<Integer> jobID = result.getJobIDs();
		UserModel userModel = OurGridPortal.getUserModel();
		for (Integer integer : jobID) {
			userModel.addJobId(getJobViewId(), integer);
		}
		
	}

	protected abstract void processStatus();
	
	protected abstract void processInputs(List<?> inputs);

	protected void activateStatus() {
		this.jobStatusPanel.setVisible(true);
		this.jobStatusFieldSet.setVisible(true);
		this.jobStatusPanel.scheduleJobDescriptionRepeatedAction();
	}

	public void setTabItem(TabItem tabItem) {
		this.tabItem = tabItem;
	}

	public TabItem getTabItem() {
		return tabItem;
	}

	public void setJobTabCount(Integer jobTabCount) {
		this.jobTabCount = jobTabCount;
	}

	public Integer getJobTabCount() {
		return jobTabCount;
	}

	public void setJobViewId(Integer jobViewId) {
		this.jobViewId = jobViewId;
	}

	public Integer getJobViewId() {
		return jobViewId;
	}
	
}