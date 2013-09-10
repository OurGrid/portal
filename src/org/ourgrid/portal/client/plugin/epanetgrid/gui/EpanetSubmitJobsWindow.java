package org.ourgrid.portal.client.plugin.epanetgrid.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.reservatorios.gui.ReservatoriosSubmitJobsHelpWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class EpanetSubmitJobsWindow extends PluginSubmitJobsWindow {

	public EpanetSubmitJobsWindow(List<AbstractRequest> list) {
		super(list, "EpanetGrid", new EpanetCreateJobPanel());
		initializeVariables();
	}
	
	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new ReservatoriosSubmitJobsHelpWindow("Submit EpanetGrid Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			EpanetJobSubmissionPanel panel = (EpanetJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			EpanetJobSubmissionPanel panel = (EpanetJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}
