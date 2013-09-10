package org.ourgrid.portal.client.plugin.autodock.gui;

import org.ourgrid.portal.client.common.gui.JobStatusPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

public class AutodockJobStatusPanel extends JobStatusPanel {

	
	public AutodockJobStatusPanel(Integer jobViewId, Integer tabCount, TabItem tabItem) {
		super(jobViewId, tabCount, tabItem);
		
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
	}
	
}