package org.ourgrid.portal.client.plugin.blender.gui;

import org.ourgrid.portal.client.common.gui.JobStatusPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

public class BlenderJobStatusPanel extends JobStatusPanel {

	
	public BlenderJobStatusPanel(Integer jobViewId, Integer tabCount, TabItem tabItem) {
		super(jobViewId, tabCount, tabItem);
		
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
	}
	
}