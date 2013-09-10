package org.ourgrid.portal.client.plugin.rho.gui;

import org.ourgrid.portal.client.common.gui.JobStatusPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

public class RhoJobStatusPanel extends JobStatusPanel {

	
	public RhoJobStatusPanel(Integer jobViewId, Integer tabCount, TabItem tabItem) {
		super(jobViewId, tabCount, tabItem);
		
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
	}
	
}