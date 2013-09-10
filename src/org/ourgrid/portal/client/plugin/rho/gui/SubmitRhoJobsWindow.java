package org.ourgrid.portal.client.plugin.rho.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class SubmitRhoJobsWindow extends PluginSubmitJobsWindow {
	
	public SubmitRhoJobsWindow(List<AbstractRequest> list) {
		
		super(list,"Submit SLinCA Jobs", new RhoJobPanel());
		initializeVariables();
	}
	
	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new SubmitRhoJobsHelpWindow("Submit Rho Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			RhoJobSubmissionPanel panel = (RhoJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			RhoJobSubmissionPanel panel = (RhoJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}