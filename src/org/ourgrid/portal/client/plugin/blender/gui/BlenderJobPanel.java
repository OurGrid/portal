package org.ourgrid.portal.client.plugin.blender.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.blender.gui.model.BlenderJobRequest;
import org.ourgrid.portal.client.plugin.blender.to.model.BlenderInputFileTO;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class BlenderJobPanel extends PluginCreateJobPanel {
	 
	public BlenderJobPanel() {
		super();
	}


	@Override
	protected BlenderJobSubmissionPanel createJobSubmitPanel(int jobViewId, TabItem item) {
		return new BlenderJobSubmissionPanel(jobTabCounter, jobViewId, item);
	}
	
	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		BlenderJobRequest blenderRequest = (BlenderJobRequest) request;
		
		userModel.addJobId(jobViewId, jobID);
		userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
		
		return addTabJob(jobViewId,false,blenderRequest.getInputFiles());
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<BlenderInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}

}