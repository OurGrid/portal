package org.ourgrid.portal.client.plugin.cisternas.gui;

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
import org.ourgrid.portal.client.plugin.cisternas.to.model.CisternasInputFileTO;
import org.ourgrid.portal.client.plugin.cisternas.to.response.CisternasJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.cisternas.to.service.CisternasJobSubmissionTO;
import org.ourgrid.portal.client.plugin.cisternas.util.CisternasJobSubmissionMessages;
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
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CisternasJobSubmissionPanel extends PluginJobSubmissionPanel {
	
	private FieldSet						      mainFieldSet;
	private FieldSet 							  typeSelectFieldSet;	
	private FieldSet 							  inputFilesFieldSet;
	private FieldSet 							  cisternasFileFieldSet;
	private FieldSet 							  inicialVolumeFieldSet;
	
	private NumberField							  initialVolumeNumberField;
	
	private FormPanel							  fileSubmissionPanel;
	
	private OurFileUploadField 					  cisternasFileUploadField;
	
	private Radio 								  runCompleteButton;
//	private Radio 								  runStandardButton;
	private Radio								  runVariationsButton;
//	private Radio								  runConsenseButton;
	
	private RadioGroup 							  runGroup;
	
	private Button								  submitButton;
	
	public CisternasJobSubmissionPanel(Integer jobTabCounter, int jobViewId, TabItem container) {
		super(jobTabCounter, jobViewId, container);
	}
	
	/**
	 * Method that creates all panels and fieldsets
	 */
	@Override
	protected void init() {
		
		createMainPanel();
		createMainFieldSet();
		
		createSubmissionTypeSelectFieldSet();
		createSelectionButtons();
		
		createInputFilesFieldSet();
		createAndAddFileSubmissionPanel();
		
		createInicialVolumeFieldSet();
		createInicialVolumeNumberField();
		
		createSubmitButton();
		
		createStatusFieldSet("Cisternas Status");
		createStatusPanel(new CisternasJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
		addListeners();
	}
	
	//GUI
	
	@Override
	public void desactivateSubmission() {
	}
	
	@Override
	protected void activateSubmission() {
	}
	
	private void createMainFieldSet() {
		mainFieldSet = new FieldSet();
		mainFieldSet.setExpanded(true);
		mainFieldSet.setCollapsible(true);
		mainFieldSet.setWidth(SUBMIT_FIELDSET_WIDTH);
		
		mainPanel.add(mainFieldSet);
	}
	
	private void createSubmissionTypeSelectFieldSet() {
		typeSelectFieldSet = new FieldSet();
		typeSelectFieldSet.setHeading("Execution Type");
		typeSelectFieldSet.setExpanded(true);
		typeSelectFieldSet.setWidth(720);
		
		mainFieldSet.add(typeSelectFieldSet);
	}
	
	private void createSelectionButtons() {
		runCompleteButton = new Radio();
		runCompleteButton.setBoxLabel("Complete");
		runCompleteButton.setVisible(true);
		runCompleteButton.setValue(true);

		runVariationsButton = new Radio();
		runVariationsButton.setBoxLabel("Varying");
		runVariationsButton.setVisible(true);
		
		runGroup = new RadioGroup();
		runGroup.add(runCompleteButton);
		runGroup.add(runVariationsButton);
		runGroup.setShadow(true);
		runGroup.setOriginalValue(runCompleteButton);
		
		
		typeSelectFieldSet.add(runGroup);
	}
	
	private void createInputFilesFieldSet() {
		inputFilesFieldSet = new FieldSet();
		inputFilesFieldSet.setHeading("Input Files");
		inputFilesFieldSet.setExpanded(true);
		inputFilesFieldSet.setWidth(720);
		
		mainFieldSet.add(inputFilesFieldSet);
	}
	
	private void createAndAddFileSubmissionPanel() {

		fileSubmissionPanel = new FormPanel();
		fileSubmissionPanel.setHeaderVisible(false);
		fileSubmissionPanel.setBorders(false);
		fileSubmissionPanel.setBodyBorder(false);

		LayoutContainer container = new LayoutContainer();
		container.setLayout(new RowLayout());
		
		createInputFilesSpots();
		
		container.add(getCisternaFileFieldSet());

		fileSubmissionPanel.add(container);
		inputFilesFieldSet.add(fileSubmissionPanel);
		

	}

	
	
	private void createInputFilesSpots() {
		setInputFilesFieldSet();
		
		createInputFilesFieldSet(getCisternaFileFieldSet(), "Cisterns File");
		
		setInputFilesSpots();
		createReservatorioInputFilesSpots();
	}

	private void setInputFilesFieldSet() {
		setReservatorioFileFieldSet(new FieldSet());
	}
	
	private void createInputFilesFieldSet(FieldSet field, String heading) {
		field.setHeading(heading);
		field.setExpanded(true);
		field.setBorders(false);
		field.setWidth(720);
		
		inputFilesFieldSet.add(field);
	}
	
	private void createInicialVolumeFieldSet() {
		inicialVolumeFieldSet = new FieldSet();
		inicialVolumeFieldSet.setHeading("Inicial Volume");
		inicialVolumeFieldSet.setExpanded(true);
		inicialVolumeFieldSet.setWidth(720);
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(200);
		
		inicialVolumeFieldSet.setLayout(layout);
		
		mainFieldSet.add(inicialVolumeFieldSet);
	}
	
	private void setInputFilesSpots() {
		setReservatorioFileUploadField(new OurFileUploadField());
	}
	
	private void createReservatorioInputFilesSpots() {
		cisternasFileUploadField.setTitle("Cistern File");
		
		cisternasFileUploadField.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validReservatorioFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", CisternasJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_CISTERNAS, null);
					return;
				}
				requestFileUpload();
				
			}
		});
		
		cisternasFileFieldSet.add(cisternasFileUploadField);
	}
	
	private void createInicialVolumeNumberField() {
		initialVolumeNumberField = new NumberField();
		initialVolumeNumberField.setFieldLabel("Inicial Volume %");
		
		inicialVolumeFieldSet.add(initialVolumeNumberField);
	}
	
	//Interface Execution
	
	private void createSubmitButton() {
		submitButton = new Button("Submit");
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent ce) {
				if(validateInputs()) {
					submitButton.disable();
					maskMainPanel("Wait while we process the inputs");
					submitJob();
				}
			}
			
		});
		
		mainFieldSet.add(submitButton);
	}
	
	private void requestFileUpload() {

		UserModel userModel = OurGridPortal.getUserModel();
		ServiceTO serviceTO = createUploadSessionIdTO(userModel.getUserLogin());
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<UploadSessionIDResponseTO>() {

			public void onSuccess(UploadSessionIDResponseTO result) {
				sendUploadedFile(result.getUploadId());
//				UserModel userModel = OurGridPortal.getUserModel();
//				createStatusPanel(new CisternasJobStatusPanel(jobViewId, jobTabCount, tabItem, result.getUploadId(), userModel.getUserLogin()));
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
		cisternasFileUploadField.setName(uploadId.toString());

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

	private boolean validReservatorioFileName() {
		return (cisternasFileUploadField.getFilename().endsWith(".zip"));
	}
	
	private boolean validateInputs() {
		
		if(cisternasFileUploadField.getFilename() == null ) {
			MessageBox.alert("Invalid Format", CisternasJobSubmissionMessages.MISSING_FILE, null);
			return false;
		}else if(!validReservatorioFileName()) {
			MessageBox.alert("Invalid Format", CisternasJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_CISTERNAS, null);
			return false;
		}
		return true;
		
	}
	
	private CisternasJobSubmissionTO createCisternasJobSubmissionTO(Long uploadSessionId, String userLogin,String type, Double initialVolume) {
		
		CisternasJobSubmissionTO cisternasJobSubmissionTO = new CisternasJobSubmissionTO();
		
		cisternasJobSubmissionTO.setExecutorName(CommonServiceConstants.CISTERNAS_JOB_SUBMISSION_EXECUTOR);
		cisternasJobSubmissionTO.setUploadId(uploadSessionId);
		cisternasJobSubmissionTO.setUserLogin(userLogin);
		cisternasJobSubmissionTO.setInputFile(createInputFileTO(uploadSessionId,type,initialVolume));
		cisternasJobSubmissionTO.setJobName("Cisterns Job");
		cisternasJobSubmissionTO.setEmailNotification(false);
		
		return cisternasJobSubmissionTO;
	}

	private CisternasInputFileTO createInputFileTO(Long uploadID, String type, Double initialVolume ) {
		
		CisternasInputFileTO inputFileTO = new CisternasInputFileTO();
		
		inputFileTO.setInitialVolume(initialVolume);
		inputFileTO.setUploadID(uploadID);
		inputFileTO.setType(type);
		
		return inputFileTO;
	}
	
	private void submitJob() {
		UserModel userModel = OurGridPortal.getUserModel();
		
		Double initialValue = Double.parseDouble(initialVolumeNumberField.getValue().toString());
		String type = runGroup.getValue().getBoxLabel();
		CisternasJobSubmissionTO jobSubmissionTO = createCisternasJobSubmissionTO(userModel.getUploadSessionId(),userModel.getUserLogin(),type,initialValue );
		
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<CisternasJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(CisternasJobSubmissionResponseTO result) {
				unmaskMainPanel();
				doLayout();
				processJobSubmissionResponse(result);
				processStatus();
			}
		});
	}
	
	public static void generateZipDownload() {
		
	}
	
	@Override
	protected void processStatus() {
		showJobStatusTree();
		submitButton.enable();
	}
	
	/**
	 * Method that show the jobstatustree
	 */
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

	public FieldSet getCisternaFileFieldSet() {
		return cisternasFileFieldSet;
	}

	public void setReservatorioFileFieldSet(FieldSet reservatorioFileFieldSet) {
		this.cisternasFileFieldSet = reservatorioFileFieldSet;
	}

	public OurFileUploadField getCisternasFileUploadField() {
		return cisternasFileUploadField;
	}

	public void setReservatorioFileUploadField(
			OurFileUploadField reservatorioFileUploadField) {
		this.cisternasFileUploadField = reservatorioFileUploadField;
	}

	private void unmaskMainPanel() {
		this.el().unmask();
	}
	
	private void maskMainPanel(String message) {
		this.el().mask(message);
	}
	
	// OPERATIONS
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

	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();		
	}
	
}

