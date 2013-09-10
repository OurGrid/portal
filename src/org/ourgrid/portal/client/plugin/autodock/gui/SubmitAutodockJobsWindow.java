package org.ourgrid.portal.client.plugin.autodock.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class SubmitAutodockJobsWindow extends PluginSubmitJobsWindow {
	
	public SubmitAutodockJobsWindow(List<AbstractRequest> list) {
		super(list, "Submit Autodock Jobs", new AutodockJobPanel());
		initializeVariables();
	}
	

	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new SubmitAutodockJobsHelpWindow("Submit Autodock Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			AutodockJobSubmissionPanel panel = (AutodockJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			AutodockJobSubmissionPanel panel = (AutodockJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}
}