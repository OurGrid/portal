package org.ourgrid.portal.client.plugin.marbs.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class MarbsSubmitJobsWindow extends PluginSubmitJobsWindow {

	public MarbsSubmitJobsWindow(List<AbstractRequest> list) {
		super(list, "Marbs", new MarbsCreateJobPanel());
		// TODO Auto-generated constructor stub
	}


	
	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new MarbsSubmitJobsHelpWindow("Submit Marbs Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			MarbsJobSubmissionPanel panel = (MarbsJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			MarbsJobSubmissionPanel panel = (MarbsJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}
