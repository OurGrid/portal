package org.ourgrid.portal.client.plugin.lvc.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class SubmitLvcJobsWindow extends PluginSubmitJobsWindow {
	
	public SubmitLvcJobsWindow(List<AbstractRequest> list) {
		
		super(list,"Submit LVC Jobs", new LvcJobPanel());
		initializeVariables();
	}
	
	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new SubmitLvcJobsHelpWindow("Submit Lvc Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			LvcJobSubmissionPanel panel = (LvcJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			LvcJobSubmissionPanel panel = (LvcJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}