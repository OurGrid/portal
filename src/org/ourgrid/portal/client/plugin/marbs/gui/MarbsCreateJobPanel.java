package org.ourgrid.portal.client.plugin.marbs.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;
import org.ourgrid.portal.client.plugin.marbs.gui.model.MarbsJobRequest;
import org.ourgrid.portal.client.plugin.marbs.to.model.MarbsInputFileTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class MarbsCreateJobPanel extends PluginCreateJobPanel{

	public MarbsCreateJobPanel() {
		super();
	}
	
	@Override
	protected MarbsJobSubmissionPanel createJobSubmitPanel(int jobViewId,TabItem container) {
		return new MarbsJobSubmissionPanel(jobTabCounter,jobViewId, container);
	}
	

	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		MarbsJobRequest marbsRequest = (MarbsJobRequest) request;
		
		List<MarbsInputFileTO> input = new LinkedList<MarbsInputFileTO>();
		input.add(marbsRequest.getInputFile());
		
		userModel.addJobId(jobViewId, jobID);
		userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
		
		return addTabJob(jobViewId, false, input);
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<MarbsInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}

}
