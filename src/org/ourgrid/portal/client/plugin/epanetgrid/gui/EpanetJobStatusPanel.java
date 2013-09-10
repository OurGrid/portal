package org.ourgrid.portal.client.plugin.epanetgrid.gui;

import org.ourgrid.portal.client.common.gui.JobStatusPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

public class EpanetJobStatusPanel extends JobStatusPanel {

	public EpanetJobStatusPanel(Integer jobViewId, Integer tabCount,
			TabItem container) {
		super(jobViewId, tabCount, container);
		
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
	}

}
