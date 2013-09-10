package org.ourgrid.portal.client.plugin.lvc.gui;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.JobListener;
import org.ourgrid.portal.client.common.gui.JobStatusPanel;
import org.ourgrid.portal.client.common.to.model.JobTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.plugin.lvc.to.response.LvcJobImageJoinerResponseTO;
import org.ourgrid.portal.client.plugin.lvc.to.service.LvcJobImageJoinerTO;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LvcJobStatusPanel extends JobStatusPanel {
	
	public LvcJobStatusPanel(Integer jobViewId, Integer tabCount, TabItem tabItem) {
		super(jobViewId, tabCount, tabItem);
		
		this.jobStatusTreePanel.setHeight(219);
		this.nodeDetails.setHeight(219);
		this.nodeDetails.setBorders(false);
		
		registerJobListener(new JobListener() {
			
			@Override
			public void jobFinished(JobTO jobTO) {
				LvcJobImageJoinerTO to = createLvcJobImageJoinerTO();
				
				OurGridPortalServerUtil.getInstance().execute(to, new AsyncCallback<LvcJobImageJoinerResponseTO>() {

					@Override
					public void onFailure(Throwable result) {
						result.printStackTrace();
						MessageBox.alert("Lvc Image Joiner Error", result.getMessage(), null);
					}

					@Override
					public void onSuccess(LvcJobImageJoinerResponseTO result) {
						OurGridPortal.refreshFileExplorer();
						if(result.isWasFinalImageGenerated()){
							MessageBox.alert("Image process successfully completed", "The image was successfully processed, please, check it on the file explorer", null);
						}
					}
				});
			}
		});
	}
	
	private LvcJobImageJoinerTO createLvcJobImageJoinerTO(){
		LvcJobImageJoinerTO to = new LvcJobImageJoinerTO();
		to.setExecutorName(CommonServiceConstants.LVC_JOB_IMAGE_JOINER_EXECUTOR);
		to.setJobId(getJobId());
		
		return to;
	}
	
}