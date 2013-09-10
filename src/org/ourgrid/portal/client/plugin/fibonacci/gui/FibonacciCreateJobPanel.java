package org.ourgrid.portal.client.plugin.fibonacci.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.fibonacci.gui.model.FibonacciJobRequest;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;
import org.ourgrid.portal.client.plugin.rho.gui.RhoJobSubmissionPanel;
import org.ourgrid.portal.client.plugin.rho.gui.model.RhoJobRequest;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class FibonacciCreateJobPanel extends PluginCreateJobPanel {

	public FibonacciCreateJobPanel() {
		super();
	}
	
	@Override
	protected FibonacciJobSubmissionPanel createJobSubmitPanel(int jobViewId,TabItem container) {
		return new FibonacciJobSubmissionPanel(jobTabCounter,jobViewId, container);
	}
	
	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		FibonacciJobRequest fibRequest = (FibonacciJobRequest) request;
		
		userModel.addJobId(jobViewId, jobID);
		userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
		
		return addTabJob(jobViewId, false, new LinkedList<Object>());
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<Object>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}
}
