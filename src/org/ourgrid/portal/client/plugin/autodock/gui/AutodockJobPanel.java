package org.ourgrid.portal.client.plugin.autodock.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.autodock.gui.model.AutodockJobRequest;
import org.ourgrid.portal.client.plugin.autodock.to.model.AutodockInputFileTO;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class AutodockJobPanel extends PluginCreateJobPanel {
	 
	public AutodockJobPanel() {
		super();
	}


	@Override
	protected AutodockJobSubmissionPanel createJobSubmitPanel(int jobViewId, TabItem item) {
		return new AutodockJobSubmissionPanel(jobTabCounter, jobViewId, item);
	}
	
	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		AutodockJobRequest autodockRequest = (AutodockJobRequest) request;
		
		userModel.addJobId(jobViewId, jobID);
		
		List<AutodockInputFileTO> inputs = new LinkedList<AutodockInputFileTO>();
		inputs.add(autodockRequest.getMacromoleculeRigidModel());
		inputs.add(autodockRequest.getMacromoleculeFlexibleModel());
		inputs.add(autodockRequest.getGridParameters());
		inputs.add(autodockRequest.getLigantModel());
		inputs.add(autodockRequest.getDockingParameters());
		
		return addTabJob( jobViewId, false, inputs );
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<AutodockInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}


}