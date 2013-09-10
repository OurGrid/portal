package org.ourgrid.portal.client.plugin.genecodis.gui;

import org.ourgrid.portal.client.common.gui.JobStatusPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

public class GenecodisJobStatusPanel extends JobStatusPanel {

	public GenecodisJobStatusPanel(Integer jobViewId, Integer tabCount, TabItem container) {
		super(jobViewId, tabCount, container);

		this.setHeight(250);
		this.setWidth(650);
		
		this.jobStatusTreePanel.setHeight(210);
		this.nodeDetails.setHeight(210);
		this.nodeDetails.setBorders(false);
	}
	
}