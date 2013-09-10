package org.ourgrid.portal.client.plugin.genecodis.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class SubmitGenecodisJobsWindow extends PluginSubmitJobsWindow {
	
	public SubmitGenecodisJobsWindow(List<AbstractRequest> list) {
		super(list,"Submit Genecodis Jobs", new GenecodisJobPanel());
		initializeVariables();
	}

	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new SubmitGenecodisJobsHelpWindow("Submit Genecodis Jobs Help");
	}

	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			GenecodisJobSubmissionPanel panel = (GenecodisJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}
	
	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			GenecodisJobSubmissionPanel panel = (GenecodisJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}