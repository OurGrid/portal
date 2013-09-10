package org.ourgrid.portal.client.plugin.marbs.gui;

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
import org.ourgrid.portal.client.plugin.marbs.to.model.MarbsInputFileTO;
import org.ourgrid.portal.client.plugin.marbs.to.response.MarbsJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.marbs.to.service.MarbsJobSubmissionTO;
import org.ourgrid.portal.client.plugin.marbs.util.MarbsJobSubmissionMessages;

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

public class MarbsJobSubmissionPanel extends PluginJobSubmissionPanel{

	private FieldSet			mainFieldSet;
	private FieldSet			solosFieldSet;
	private FieldSet			culturasFieldSet;
	private FieldSet			precipitacaoFieldSet;
	private FieldSet 			inputFilesFieldSet;
	
	private OurFileUploadField 	solosFileSpot;
	private OurFileUploadField 	culturasFileSpot;
	private OurFileUploadField 	precipitacaoFileSpot;
	
	private FormPanel			fileSubmissionPanel;
	
	private Button				submitButton;

	public MarbsJobSubmissionPanel(Integer jobTabCounter, int jobViewId,TabItem container) {
		super(jobTabCounter, jobViewId, container);
	}
	
	
	@Override
	protected void init() {

		createMainPanel();
		createMainFieldSet();
		
		createInputFilesFieldSet();
		createAndAddFileSubmissionPanel();
		
		createSubmitButton();
		
		createStatusFieldSet("Marbs Status");
		createStatusPanel(new MarbsJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
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
		
		container.add(getSolosFieldSet());
		container.add(getPrecipitacaoFieldSet());
		container.add(getCulturasFieldSet());

		fileSubmissionPanel.add(container);
		inputFilesFieldSet.add(fileSubmissionPanel);
	}
	
	public FieldSet getSolosFieldSet() {
		return solosFieldSet;
	}


	public void setSolosFieldSet(FieldSet solosFieldSet) {
		this.solosFieldSet = solosFieldSet;
	}


	public FieldSet getCulturasFieldSet() {
		return culturasFieldSet;
	}


	public void setCulturasFieldSet(FieldSet culturasFieldSet) {
		this.culturasFieldSet = culturasFieldSet;
	}


	public FieldSet getPrecipitacaoFieldSet() {
		return precipitacaoFieldSet;
	}


	public void setPrecipitacaoFieldSet(FieldSet precipitacaoFieldSet) {
		this.precipitacaoFieldSet = precipitacaoFieldSet;
	}


	public OurFileUploadField getSolosFileSpot() {
		return solosFileSpot;
	}


	public void setSolosFileSpot(OurFileUploadField solosFileSpot) {
		this.solosFileSpot = solosFileSpot;
	}


	public OurFileUploadField getCulturasFileSpot() {
		return culturasFileSpot;
	}


	public void setCulturasFileSpot(OurFileUploadField culturasFileSpot) {
		this.culturasFileSpot = culturasFileSpot;
	}


	public OurFileUploadField getPrecipitacaoFileSpot() {
		return precipitacaoFileSpot;
	}


	public void setPrecipitacaoFileSpot(OurFileUploadField precipitacaoFileSpot) {
		this.precipitacaoFileSpot = precipitacaoFileSpot;
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
		setSolosFieldSet(new FieldSet());
		setPrecipitacaoFieldSet(new FieldSet());
		setCulturasFieldSet(new FieldSet());
	}
	
	private void createInputFilesSpots() {
		setInputFilesFieldSet();
		
		createInputFilesFieldSet(getSolosFieldSet(), "Soil File");
		createInputFilesFieldSet(getPrecipitacaoFieldSet(), "Precipitation File");
		createInputFilesFieldSet(getCulturasFieldSet(), "Cultivation File");
		
		setInputFilesSpots();
		createSolosInputFilesSpots();
		createCulturasInputFilesSpots();
		createPrecipitacaoInputFilesSpots();
	}
	
	private void createSolosInputFilesSpots() {
		solosFileSpot.setTitle("Soil File");
		
		solosFileSpot.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validSolosFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", MarbsJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_SOLOS, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getSolosFileSpot(),getPrecipitacaoFileSpot(), getCulturasFileSpot())) {
					requestFileUpload();
				}
				
			}
		});
		
		solosFieldSet.add(solosFileSpot);
	}
	
	private void createCulturasInputFilesSpots() {
		culturasFileSpot.setTitle("Cultivation File");

		culturasFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validCulturasFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", MarbsJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_CULTURAS, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getSolosFileSpot(),getPrecipitacaoFileSpot(), getCulturasFileSpot())) {
					requestFileUpload();
				}

			}
		});

		culturasFieldSet.add(culturasFileSpot);

	}
	
	private void createPrecipitacaoInputFilesSpots() {
		precipitacaoFileSpot.setTitle("Precipitation File");

		precipitacaoFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validPrecipitacaoFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", MarbsJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_PMH, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getSolosFileSpot(),getPrecipitacaoFileSpot(), getCulturasFileSpot())) {
					requestFileUpload();
				}

			}
		});

		precipitacaoFieldSet.add(precipitacaoFileSpot);

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

		if(solosFileSpot.getFilename() == null || 
				culturasFileSpot.getFilename() == null ||
				precipitacaoFileSpot.getFilename() == null) {
			MessageBox.alert("Invalid Format", MarbsJobSubmissionMessages.MISSING_FILE, null);
			return false;
		} 
		return true;
	}
	
	private void submitJob() {
		UserModel userModel = OurGridPortal.getUserModel();
		
		MarbsJobSubmissionTO jobSubmissionTO = createMarbsJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<MarbsJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(MarbsJobSubmissionResponseTO result) {
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
	
	protected void desactivateSubmitFieldSet() {
  		mainFieldSet.setExpanded(false);
  		doLayout();
	}
	
	
	private MarbsJobSubmissionTO createMarbsJobSubmissionTO(Long uploadSessionId,	String userLogin) {
		
		MarbsJobSubmissionTO marbsJobSubmissionTO = new MarbsJobSubmissionTO();
		
		marbsJobSubmissionTO.setExecutorName(CommonServiceConstants.MARBS_JOB_SUBMISSION_EXECUTOR);
		marbsJobSubmissionTO.setUploadId(uploadSessionId);
		marbsJobSubmissionTO.setUserLogin(userLogin);
		marbsJobSubmissionTO.setInputFile(createInputFileTO(uploadSessionId));
		marbsJobSubmissionTO.setJobName("Marbs Job");
		marbsJobSubmissionTO.setEmailNotification(false);
		
		return marbsJobSubmissionTO;
	}

	private MarbsInputFileTO createInputFileTO(Long uploadSessionId) {
		MarbsInputFileTO inputFileTO = new MarbsInputFileTO();
		
		inputFileTO.setUploadID(uploadSessionId);
		
		return inputFileTO;
	}


	
	
	private boolean verifyIfAbleToSendUpload(OurFileUploadField uploadFieldA, OurFileUploadField uploadFieldB, 
			OurFileUploadField uploadFieldC) {
		return (uploadFieldA.getFilename().equals("") || uploadFieldB.getFilename().equals("") || uploadFieldC.getFilename().equals(""));
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
		solosFileSpot.setName(uploadId.toString());
		precipitacaoFileSpot.setName(uploadId.toString());
		culturasFileSpot.setName(uploadId.toString());
		
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
	
	private boolean validSolosFileName() {
		return (solosFileSpot.getFilename().endsWith(".sol"));
	}
	
	private boolean validCulturasFileName() {
		return (culturasFileSpot.getFilename().endsWith(".ras"));
	}
	
	private boolean validPrecipitacaoFileName() {
		return (precipitacaoFileSpot.getFilename().endsWith(".pmh"));
	}
	
	private void setInputFilesSpots() {
		setSolosFileSpot(new OurFileUploadField());
		setPrecipitacaoFileSpot(new OurFileUploadField());
		setCulturasFileSpot(new OurFileUploadField());
	}


	@Override
	public void desactivateSubmission() {
	}
	
	@Override
	protected void activateSubmission() {
	}

	private void unmaskMainPanel() {
		this.el().unmask();
	}


	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();		
	}

}
