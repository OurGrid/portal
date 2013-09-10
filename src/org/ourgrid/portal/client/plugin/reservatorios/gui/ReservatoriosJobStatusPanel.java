package org.ourgrid.portal.client.plugin.reservatorios.gui;

import org.ourgrid.portal.client.common.JobListener;
import org.ourgrid.portal.client.common.gui.JobStatusPanel;
import org.ourgrid.portal.client.common.to.model.JobTO;

import com.extjs.gxt.ui.client.widget.TabItem;

public class ReservatoriosJobStatusPanel extends JobStatusPanel {
	
	public ReservatoriosJobStatusPanel(Integer jobViewId, Integer tabCount,	TabItem container) {
		
		super(jobViewId, tabCount, container);
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
		
		registerJobListener(new JobListener() {
			
			@Override
			public void jobFinished(JobTO jobTO) {
			}
		});
	}
	
}
