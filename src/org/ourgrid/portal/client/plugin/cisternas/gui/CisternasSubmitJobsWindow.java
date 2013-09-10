package org.ourgrid.portal.client.plugin.cisternas.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class CisternasSubmitJobsWindow extends PluginSubmitJobsWindow{

	public CisternasSubmitJobsWindow(List<AbstractRequest> list) {
		super(list, "Cisternas", new CisternasJobPanel());
		initializeVariables();
	}
	
	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new CisternasSubmitJobsHelpWindow("Submit Reservatorios Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			CisternasJobSubmissionPanel panel = (CisternasJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			CisternasJobSubmissionPanel panel = (CisternasJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}
