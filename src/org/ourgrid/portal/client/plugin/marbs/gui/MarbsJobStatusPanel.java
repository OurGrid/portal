package org.ourgrid.portal.client.plugin.marbs.gui;

import org.ourgrid.portal.client.common.gui.JobStatusPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

public class MarbsJobStatusPanel extends JobStatusPanel {

	public MarbsJobStatusPanel(Integer jobViewId, Integer tabCount,
			TabItem container) {
		super(jobViewId, tabCount, container);
		
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
	}

}
