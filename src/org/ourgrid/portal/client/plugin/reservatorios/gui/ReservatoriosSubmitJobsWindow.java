package org.ourgrid.portal.client.plugin.reservatorios.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class ReservatoriosSubmitJobsWindow extends PluginSubmitJobsWindow{

	public ReservatoriosSubmitJobsWindow(List<AbstractRequest> list) {
		super(list, "Reservatorios", new ReservatoriosJobPanel());
		initializeVariables();
	}
	
	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new ReservatoriosSubmitJobsHelpWindow("Submit Reservatorios Jobs Help");
	}
	
	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			ReservatoriosJobSubmissionPanel panel = (ReservatoriosJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
		
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			ReservatoriosJobSubmissionPanel panel = (ReservatoriosJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
	}

}
