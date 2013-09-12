package org.ourgrid.portal.client.plugin.genecodis.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.genecodis.gui.model.GenecodisJobRequest;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class GenecodisJobPanel extends PluginCreateJobPanel {

	public GenecodisJobPanel() {
		super();
	}

	@Override
	protected GenecodisJobSubmissionPanel createJobSubmitPanel(int jobViewId, TabItem container) {
		return new GenecodisJobSubmissionPanel(jobTabCounter, jobViewId, container);
	}

	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		GenecodisJobRequest genecodisRequest = (GenecodisJobRequest) request;
		
		userModel.addJobId(jobViewId, jobID);
		
		return addTabJob(jobViewId, false, genecodisRequest.getInputFiles());
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<GenecodisInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}
}