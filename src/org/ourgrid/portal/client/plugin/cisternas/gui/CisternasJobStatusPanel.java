package org.ourgrid.portal.client.plugin.cisternas.gui;

import org.ourgrid.portal.client.common.JobListener;
import org.ourgrid.portal.client.common.gui.JobStatusPanel;
import org.ourgrid.portal.client.common.to.model.JobTO;

import com.extjs.gxt.ui.client.widget.TabItem;

public class CisternasJobStatusPanel extends JobStatusPanel {
	
//	private long uploadID;
//	private String userLogin;
	
	public CisternasJobStatusPanel(Integer jobViewId, Integer tabCount,	TabItem container) {
		
		super(jobViewId, tabCount, container);
		
//		this.setUserLogin(userLogin);
//		this.setUploadID(uploadID);
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
		
		registerJobListener(new JobListener() {
			
			@Override
			public void jobFinished(JobTO jobTO) {
			}
		});
	}
	
//	public void setUploadID(long uploadID) {
//		this.uploadID = uploadID;
//	}
//
//	public long getUploadID() {
//		return uploadID;
//	}
//
//	public String getUserLogin() {
//		return userLogin;
//	}
//
//	public void setUserLogin(String userLogin) {
//		this.userLogin = userLogin;
//	}
//	
}
