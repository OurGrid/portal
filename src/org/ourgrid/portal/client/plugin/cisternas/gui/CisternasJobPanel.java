package org.ourgrid.portal.client.plugin.cisternas.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.cisternas.gui.model.CisternasJobRequest;
import org.ourgrid.portal.client.plugin.cisternas.to.model.CisternasInputFileTO;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class CisternasJobPanel extends PluginCreateJobPanel {
	
	public CisternasJobPanel() {
		super();
		
	}	
	@Override
	protected CisternasJobSubmissionPanel createJobSubmitPanel(int jobViewId,TabItem container) {
		return new CisternasJobSubmissionPanel(jobTabCounter,jobViewId, container);
	}
	
	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		CisternasJobRequest cisternasRequest = (CisternasJobRequest) request;
		
		List<CisternasInputFileTO> inputs = new LinkedList<CisternasInputFileTO>();
		inputs.add(cisternasRequest.getInputFile());
		
		
		userModel.addJobId(jobViewId, jobID);
		userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
		
		return addTabJob(jobViewId, false, new LinkedList<CisternasInputFileTO>());
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<CisternasInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}

}
