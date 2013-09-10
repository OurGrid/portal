package org.ourgrid.portal.client.plugin.fibonacci.gui;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;

import com.extjs.gxt.ui.client.widget.TabItem;

public class FibonacciSubmitJobsWindow extends PluginSubmitJobsWindow {

	public FibonacciSubmitJobsWindow(List<AbstractRequest> list) {
		super(list,"Submit Fibonacci Jobs", new FibonacciCreateJobPanel());
		initializeVariables();
	}

	@Override
	protected void initializeVariables() {
		submitJobsHelpWindow = new FibonacciSubmitJobsHelpWindow("Submit Fibonacci Jobs Help");
	}

	@Override
	public void desactivateSubmission() {
		for (TabItem tab : tabs) {
			FibonacciJobSubmissionPanel panel = (FibonacciJobSubmissionPanel) tab.getItem(0);
			panel.desactivateSubmission();
		}
		newJobButton.setEnabled(false);
	}

	@Override
	public void activateSubmission() {
		for (TabItem tab : tabs) {
			FibonacciJobSubmissionPanel panel = (FibonacciJobSubmissionPanel) tab.getItem(0);
			panel.activateSubmission();
		}
		newJobButton.setEnabled(true);
		
	}

}
