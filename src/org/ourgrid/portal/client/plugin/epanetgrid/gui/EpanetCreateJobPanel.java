package org.ourgrid.portal.client.plugin.epanetgrid.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.epanetgrid.gui.model.EpanetJobRequest;
import org.ourgrid.portal.client.plugin.epanetgrid.to.model.EpanetInputFileTO;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class EpanetCreateJobPanel extends PluginCreateJobPanel{

	public EpanetCreateJobPanel() {
		super();
	}
	
	@Override
	protected EpanetJobSubmissionPanel createJobSubmitPanel(int jobViewId,TabItem container) {
		return new EpanetJobSubmissionPanel(jobTabCounter,jobViewId, container);
	}
	

	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		EpanetJobRequest epanetRequest = (EpanetJobRequest) request;
		List<EpanetInputFileTO> input = new LinkedList<EpanetInputFileTO>();
		input.add(epanetRequest.getInputFile());
		
		userModel.addJobId(jobViewId, jobID);
		
		return addTabJob(jobViewId, false, input);
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<EpanetInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}
}
