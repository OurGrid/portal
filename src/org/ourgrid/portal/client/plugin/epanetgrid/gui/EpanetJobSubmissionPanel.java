package org.ourgrid.portal.client.plugin.epanetgrid.gui;

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
import org.ourgrid.portal.client.plugin.epanetgrid.to.model.EpanetInputFileTO;
import org.ourgrid.portal.client.plugin.epanetgrid.to.response.EpanetJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.epanetgrid.to.service.EpanetJobSubmissionTO;
import org.ourgrid.portal.client.plugin.epanetgrid.util.EpanetJobSubmissionMessages;
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EpanetJobSubmissionPanel extends PluginJobSubmissionPanel {

	private FieldSet			mainFieldSet;
	private FieldSet			malhasFieldSet;
	private FieldSet			pertubacaoFieldSet;
	private FieldSet 			inputFilesFieldSet;
	
	private OurFileUploadField 	malhasFileSpot;
	private OurFileUploadField 	pertubacaoFileSpot;
	
	private FormPanel			fileSubmissionPanel;
	
	private Button				submitButton;
	
	public EpanetJobSubmissionPanel(Integer jobTabCounter, int jobViewId,TabItem container) {
		super(jobTabCounter, jobViewId, container);
	}
	
	
	@Override
	protected void init() {

		createMainPanel();
		createMainFieldSet();
		
		createInputFilesFieldSet();
		createAndAddFileSubmissionPanel();
		
		createSubmitButton();
		
		createStatusFieldSet("EpanetGrid Status");
		createStatusPanel(new EpanetJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
		addListeners();
		
	}
	
	//INTERFACE
	
	private void createSubmitButton() {
		submitButton = new Button("Submit");
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(validateInputs()) {
					submitButton.disable();
					submitJob();
				}
			}
			
		});
		
		mainFieldSet.add(submitButton);		
	}
	
	private void createAndAddFileSubmissionPanel() {
		fileSubmissionPanel = new FormPanel();
		fileSubmissionPanel.setHeaderVisible(false);
		fileSubmissionPanel.setBorders(false);
		fileSubmissionPanel.setBodyBorder(false);

		LayoutContainer container = new LayoutContainer();
		container.setLayout(new RowLayout());
		
		createInputFilesSpots();
		
		container.add(getMalhasFieldSet());
		container.add(getPertubacaoFieldSet());

		fileSubmissionPanel.add(container);
		inputFilesFieldSet.add(fileSubmissionPanel);
	}
	
	private void createInputFilesFieldSet(FieldSet field, String heading) {
		field.setHeading(heading);
		field.setExpanded(true);
		field.setBorders(false);
		field.setWidth(720);
		
		inputFilesFieldSet.add(field);
	}


	private void createInputFilesFieldSet() {
		inputFilesFieldSet = new FieldSet();
		inputFilesFieldSet.setHeading("Input Files");
		inputFilesFieldSet.setExpanded(true);
		inputFilesFieldSet.setWidth(720);
		
		mainFieldSet.add(inputFilesFieldSet);
	}


	private void createMainFieldSet() {
		mainFieldSet = new FieldSet();
		mainFieldSet.setExpanded(true);
		mainFieldSet.setCollapsible(true);
		mainFieldSet.setWidth(SUBMIT_FIELDSET_WIDTH);
		
		mainPanel.add(mainFieldSet);		
	}
	
	private void setInputFilesFieldSet() {
		setMalhasFieldSet(new FieldSet());
		setPertubacaoFieldSet(new FieldSet());
	}
	
	private void createInputFilesSpots() {
		setInputFilesFieldSet();
		
		createInputFilesFieldSet(getMalhasFieldSet(), "Mesh File");
		createInputFilesFieldSet(getPertubacaoFieldSet(), "Pertubation File");
		
		setInputFilesSpots();
		createMalhasInputFilesSpots();
		createPertubacaoInputFilesSpots();
	}
	
	private void createMalhasInputFilesSpots() {
		malhasFileSpot.setTitle("Mesh File");
		
		malhasFileSpot.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validMalhasFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", EpanetJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_MALHAS, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMalhasFileSpot(),getPertubacaoFileSpot())) {
					requestFileUpload();
				}
				
			}
		});
		
		malhasFieldSet.add(malhasFileSpot);
	}
	
	private void createPertubacaoInputFilesSpots() {
		pertubacaoFileSpot.setTitle("Pertubation File");

		pertubacaoFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validMalhasFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", EpanetJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_PERTUBACAO, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMalhasFileSpot(),getPertubacaoFileSpot())) {
					requestFileUpload();
				}

			}
		});

		pertubacaoFieldSet.add(pertubacaoFileSpot);

	}
	
	//EXECUTION
	
	
	private void addListeners() {
		fileSubmissionPanel.addListener(Events.Submit, new Listener<FormEvent>() {

			public void handleEvent(FormEvent be) {

				if (isQuotaExceed(be.getResultHtml())) {
					MessageBox.alert("Disk quota exceeded", "The specified file exceeds your quota.", null);
				}else{
					OurGridPortal.refreshFileExplorer();
				}
			}
		});		
	}
	
	protected boolean isQuotaExceed(String resultHtml) {
		return resultHtml.contains(CommonServiceConstants.QUOTA_EXCEED_ERROR_CODE);
	}

	private boolean validateInputs() {

		if(malhasFileSpot.getFilename() == null || 
				pertubacaoFileSpot.getFilename() == null) {
			MessageBox.alert("Invalid Format", EpanetJobSubmissionMessages.MISSING_FILE, null);
			return false;
		} 
		return true;
	}
	
	private void submitJob() {
		UserModel userModel = OurGridPortal.getUserModel();
		
		EpanetJobSubmissionTO jobSubmissionTO = createEpanetJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<EpanetJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(EpanetJobSubmissionResponseTO result) {
				unmaskMainPanel();
				doLayout();
				processJobSubmissionResponse(result);
				processStatus();
			}
		});
	}
	
	@Override
	protected void processStatus() {
		showJobStatusTree();
		submitButton.enable();
	}
	
	private void showJobStatusTree() {
		desactivateSubmitFieldSet();
		activateStatus();
		this.submitButton.setEnabled(false);
	}
	
	/**
	 * Method that hides the submitFieldSet
	 */
	protected void desactivateSubmitFieldSet() {
  		mainFieldSet.setExpanded(false);
  		doLayout();
	}
	
	private EpanetJobSubmissionTO createEpanetJobSubmissionTO(Long uploadSessionId,	String userLogin) {
		
		EpanetJobSubmissionTO epanetJobSubmissionTO = new EpanetJobSubmissionTO();
		
		epanetJobSubmissionTO.setExecutorName(CommonServiceConstants.EPANET_JOB_SUBMISSION_EXECUTOR);
		epanetJobSubmissionTO.setUploadId(uploadSessionId);
		epanetJobSubmissionTO.setUserLogin(userLogin);
		epanetJobSubmissionTO.setInputFile(createInputFileTO(uploadSessionId));
		epanetJobSubmissionTO.setJobName("Epanet Job");
		epanetJobSubmissionTO.setEmailNotification(false);
		
		return epanetJobSubmissionTO;
	}

	private EpanetInputFileTO createInputFileTO(Long uploadSessionId) {
		EpanetInputFileTO inputFileTO = new EpanetInputFileTO();
		
		inputFileTO.setUploadID(uploadSessionId);
		
		return inputFileTO;
	}


	
	
	private boolean verifyIfAbleToSendUpload(OurFileUploadField uploadFieldA, OurFileUploadField uploadFieldB) {
		return (uploadFieldA.getFilename().equals("") || uploadFieldB.getFilename().equals(""));
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
		malhasFileSpot.setName(uploadId.toString());
		pertubacaoFileSpot.setName(uploadId.toString());

		String formAction = CommonServiceConstants.UPLOAD_FORM_ACTION + 
		CommonServiceConstants.USER_NAME_PARAMETER + "=" + userModel.getUserLogin() + "&" +
		CommonServiceConstants.TO_DESCOMPACT + "=true&" +
		CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=true&" +
		CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId;

		fileSubmissionPanel.setAction(formAction);
		fileSubmissionPanel.setMethod(Method.POST);
		fileSubmissionPanel.setEncoding(Encoding.MULTIPART);
		fileSubmissionPanel.submit();
	}
	
	private boolean validMalhasFileName() {
		return (malhasFileSpot.getFilename().endsWith(".inp"));
	}
	
	private void setInputFilesSpots() {
		setMalhasFileSpot(new OurFileUploadField());
		setPertubacaoFileSpot(new OurFileUploadField());
	}


	@Override
	public void desactivateSubmission() {
	}
	
	@Override
	protected void activateSubmission() {
	}

	public FieldSet getMalhasFieldSet() {
		return malhasFieldSet;
	}


	public void setMalhasFieldSet(FieldSet malhasFieldSet) {
		this.malhasFieldSet = malhasFieldSet;
	}


	public FieldSet getPertubacaoFieldSet() {
		return pertubacaoFieldSet;
	}


	public void setPertubacaoFieldSet(FieldSet pertubacaoFieldSet) {
		this.pertubacaoFieldSet = pertubacaoFieldSet;
	}


	public OurFileUploadField getMalhasFileSpot() {
		return malhasFileSpot;
	}


	public void setMalhasFileSpot(OurFileUploadField malhasFileSpot) {
		this.malhasFileSpot = malhasFileSpot;
	}


	public OurFileUploadField getPertubacaoFileSpot() {
		return pertubacaoFileSpot;
	}


	public void setPertubacaoFileSpot(OurFileUploadField pertubaaoFileSpot) {
		this.pertubacaoFileSpot = pertubaaoFileSpot;
	}
	
	private void unmaskMainPanel() {
		this.el().unmask();
	}


	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();		
	}
}