package org.ourgrid.portal.client.plugin.blender.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class SubmitBlenderJobsWindow extends PluginSubmitJobsWindow {
	
	public SubmitBlenderJobsWindow(List<AbstractRequest> list) {
		super(list, "Submit Blender Jobs", new BlenderJobPanel());
		initializeVariables();
	}
	

	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new SubmitBlenderJobsHelpWindow("Submit Rho Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			BlenderJobSubmissionPanel panel = (BlenderJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			BlenderJobSubmissionPanel panel = (BlenderJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}
}