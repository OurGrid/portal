package org.ourgrid.portal.client.plugin.lvc.gui;

import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.OurFileUploadField;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.response.UploadSessionIDResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UploadSessionIDTO;
import org.ourgrid.portal.client.common.util.OurFileUploadEventListener;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;
import org.ourgrid.portal.client.plugin.lvc.to.response.LvcJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.lvc.to.service.LvcJobSubmissionTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LvcJobSubmissionPanel extends PluginJobSubmissionPanel {
	
	private FieldSet submitFieldSet;
	private Button submitButton;
	
	private FormPanel imageFormPanel;
	private FormPanel parametersFormPanel;
	
	private OurFileUploadField imageField;
	private NumberField numberOfPartsOnHeight;
	private NumberField numberOfPartsOnWidth;
	

	public LvcJobSubmissionPanel(Integer jobTabCounter, int jobViewId,
			TabItem container) {
		
		super(jobTabCounter,jobViewId,container);
	}
	
	/**
	 * Method that creates all panels and fieldsets
	 */
	@Override
	protected void init() {
		
		createMainPanel();
		
		createSubmitFieldSet();
		createImagePanel();
  	    createParametersPanel();
  	    
  	    createSubmitButton();
		
		createStatusFieldSet("LVC Status");
		createStatusPanel(new LvcJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
	}
	
	/**
	 * Method that creates the outter fieldset
	 */
	private void createSubmitFieldSet() {
		submitFieldSet = new FieldSet();
		submitFieldSet.setHeading("Image upload and process configuration");
		submitFieldSet.setCollapsible(true);
		submitFieldSet.setWidth(SUBMIT_FIELDSET_WIDTH);
		submitFieldSet.setBorders(true);
  	  
		RowLayout layout = new RowLayout();
  	    
		submitFieldSet.setLayout(layout);
		submitFieldSet.setVisible(true);
  	    mainPanel.add(submitFieldSet);
  	    		
	}
	
	private void createParametersPanel(){
		parametersFormPanel = new FormPanel();
  	    parametersFormPanel.setHeading("Parameters");
  	    
  	    numberOfPartsOnHeight = new NumberField();
  	    numberOfPartsOnHeight.setFieldLabel("Number of parts on height");
  	    numberOfPartsOnWidth = new NumberField();
  	    numberOfPartsOnWidth.setFieldLabel("Number of parts on width");
  	    
  	    parametersFormPanel.add(numberOfPartsOnHeight);
  	    parametersFormPanel.add(numberOfPartsOnWidth);
  	    
  	    submitFieldSet.add(parametersFormPanel);
	}
	
	private void createImagePanel(){
		imageFormPanel = new FormPanel();
  	    imageFormPanel.setHeading("Image to be processed");
  	    
  	    imageField = new OurFileUploadField();
  	    imageField.setTitle("Image to be processed");
  	    
  	    imageField.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				requestFileUpload();
			}
			
		});
  	    
  	    imageFormPanel.add(imageField);
  	    
  	    submitFieldSet.add(imageFormPanel);
	}
	
	private void createSubmitButton(){
		submitButton = new Button("Submit");
	    	    
	    submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
	    	public void componentSelected(ButtonEvent ce) {
	    		submitButton.disable();
	    		submitLvcJob(numberOfPartsOnHeight.getValue().intValue(),numberOfPartsOnWidth.getValue().intValue());
	    	}
	    });
	    
	    submitFieldSet.add(submitButton);
	}
	
	
	/**
	 * Method that creates a lvcsubmissionTO
	 * @param uploadSessionId
	 * @param userLogin
	 * @return a lvcjobsubmissionTO 
	 */
	private LvcJobSubmissionTO createLvcJobSubmissionTO(Long uploadSessionId, String userLogin) {
		
		LvcJobSubmissionTO lvcJobSubmissionTO = new LvcJobSubmissionTO();
		lvcJobSubmissionTO.setExecutorName(CommonServiceConstants.LVC_JOB_SUBMISSION_EXECUTOR);
		lvcJobSubmissionTO.setUploadId(uploadSessionId);
		lvcJobSubmissionTO.setUserLogin(userLogin);
		lvcJobSubmissionTO.setJobName("LVC Job");
		
		return lvcJobSubmissionTO;
	}
	
	private void requestFileUpload() {

		UserModel userModel = OurGridPortal.getUserModel();
		ServiceTO serviceTO = createUploadSessionIdTO(userModel.getUserLogin());
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<UploadSessionIDResponseTO>() {

			public void onSuccess(UploadSessionIDResponseTO result) {
				sendUploadedFile(result.getUploadId());
			}

			public void onFailure(Throwable caught) {
				unmaskMainPanel();
				MessageBox.alert("Upload Error", caught.getMessage(), null);
			}

		});
	}
	
	private ServiceTO createUploadSessionIdTO(String userLogin) {

		UploadSessionIDTO uploadSessionIdTO = new UploadSessionIDTO();
		uploadSessionIdTO.setExecutorName(CommonServiceConstants.UPLOAD_SESSION_ID_EXECUTOR);
		uploadSessionIdTO.setUserLogin(userLogin);

		return uploadSessionIdTO;
	}
	
	private void sendUploadedFile(Long uploadId) {
		UserModel userModel = OurGridPortal.getUserModel();

		userModel.setUploadSessionId(uploadId);
		imageField.setName(uploadId.toString());

		String formAction = CommonServiceConstants.UPLOAD_FORM_ACTION + 
		CommonServiceConstants.USER_NAME_PARAMETER + "=" + userModel.getUserLogin() + "&" +
		CommonServiceConstants.TO_DESCOMPACT + "=true&" +
		CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=true&" +
		CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId;

		imageFormPanel.setAction(formAction);
		imageFormPanel.setMethod(Method.POST);
		imageFormPanel.setEncoding(Encoding.MULTIPART);
		imageFormPanel.submit();
	}

	/**
	 * Method that submit the LVC JOB
	 */
	private void submitLvcJob(int numberOfPartsOnHeight, int numberOfPartsOnWidth) {

		UserModel userModel = OurGridPortal.getUserModel();

		LvcJobSubmissionTO lvcJobSubmissionTO = createLvcJobSubmissionTO(userModel.getUploadSessionId(),
				userModel.getUserLogin());
		
		lvcJobSubmissionTO.setNumberOfPartsOnHeight(numberOfPartsOnHeight);
		lvcJobSubmissionTO.setNumberOfPartsOnWidth(numberOfPartsOnWidth);

		
		OurGridPortalServerUtil.getInstance().execute(lvcJobSubmissionTO, new AsyncCallback<LvcJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				result.printStackTrace();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(LvcJobSubmissionResponseTO result) {
				processJobSubmissionResponse(result);
				processStatus();
				OurGridPortal.refreshFileExplorer();
			}

		});
	}

	@Override
	protected void processStatus() {
		submitButton.enable();
		showJobStatusTree();
	}
	
	/**
	 * Method that show the jobstatustree
	 */
	private void showJobStatusTree() {
		desactivateSubmitFieldSet();
		
		jobStatusPanel.setVisible(true);
		jobStatusFieldSet.setVisible(true);
		jobStatusPanel.scheduleJobDescriptionRepeatedAction();
	}
	
	/**
	 * Method that hides the submitFieldSet
	 */
	protected void desactivateSubmitFieldSet() {
  		submitFieldSet.setExpanded(false);
	}

	
	@Override
	public void desactivateSubmission() {
		submitFieldSet.setEnabled(false);
	}
	@Override
	public void activateSubmission() {
		submitFieldSet.setEnabled(true);
	}

	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();		
	}
	
	private void unmaskMainPanel() {
		this.el().unmask();
	}

}