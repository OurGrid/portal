package org.ourgrid.portal.client.plugin.reservatorios.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.gui.PluginCreateJobPanel;
import org.ourgrid.portal.client.plugin.reservatorios.gui.model.ReservatoriosJobRequest;
import org.ourgrid.portal.client.plugin.reservatorios.to.model.ReservatoriosInputFileTO;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.TabItem;

public class ReservatoriosJobPanel extends PluginCreateJobPanel {
	
	public ReservatoriosJobPanel() {
		super();
		
	}	
	@Override
	protected ReservatoriosJobSubmissionPanel createJobSubmitPanel(int jobViewId,TabItem container) {
		return new ReservatoriosJobSubmissionPanel(jobTabCounter,jobViewId, container);
	}
	
	@Override
	public TabItem addTab(AbstractRequest request) {
		int jobViewId = getNextJobViewId();
		int jobID = request.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		
		ReservatoriosJobRequest reservatoriosRequest = (ReservatoriosJobRequest) request;
		List<ReservatoriosInputFileTO> input = new LinkedList<ReservatoriosInputFileTO>();
		input.add(reservatoriosRequest.getInputFile());
		
		userModel.addJobId(jobViewId, jobID);
		
		return addTabJob(jobViewId, false, input);
	}
	
	@Override
	public TabItem addTabJob() {
		TabItem tabJob = addTabJob(getNextJobViewId(), true, new LinkedList<ReservatoriosInputFileTO>());
		tabJob.setScrollMode(Scroll.AUTO);
		return tabJob;
	}

}
