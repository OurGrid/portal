package org.ourgrid.portal.client.plugin.blender.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.OurFileUploadField;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.response.UploadSessionIDResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UploadSessionIDTO;
import org.ourgrid.portal.client.common.util.OurFileUploadEventListener;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.common.util.VType;
import org.ourgrid.portal.client.common.util.VTypeValidator;
import org.ourgrid.portal.client.plugin.blender.gui.model.InputFileGridModel;
import org.ourgrid.portal.client.plugin.blender.gui.model.OutputTypeFileComboBoxModel;
import org.ourgrid.portal.client.plugin.blender.to.model.BlenderInputFileTO;
import org.ourgrid.portal.client.plugin.blender.to.response.BlenderJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.blender.to.service.BlenderJobSubmissionTO;
import org.ourgrid.portal.client.plugin.blender.util.BlenderJobSubmissionMessages;
import org.ourgrid.portal.client.plugin.blender.util.BlenderPropertiesUtil;
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BlenderJobSubmissionPanel extends PluginJobSubmissionPanel {  

	private Grid<InputFileGridModel> grid;

	private GroupingStore<InputFileGridModel>  store;

	private ColumnModel columnModel;

	private FormPanel fileSubmissionPanel;

	private ComboBox<OutputTypeFileComboBoxModel> outputTypeFile;

	private TextField<String> scenesNumberField;

	private OurFileUploadField fileUploadField;

	private CheckBox emailNotification;

	private VerticalPanel fileUploadPanel;

	private HorizontalPanel gridAndSubmitPanel;

	private FieldSet inputSubmitionFieldSet;

	private FieldSet statusSubmitionFieldSet;

	private CheckBoxSelectionModel<InputFileGridModel> sm;

	private Button submitButton;

	private Map<String, String> outputTypeMap;

	private TextField<String> startFrameNumberField;

	private TextField<String> endFrameNumberField;
	
	public BlenderJobSubmissionPanel(Integer jobTabCount, int jobViewId, TabItem container) {  

		super(jobTabCount,jobViewId,container);
	}  

	// LAYOUT
	@Override
	protected void init() {

		createMainPanel();

		createSubmitionFieldSet();  
		createStatusSubmtionFieldSet();
		createStatusFieldSet("Blender Submission Status");

		createAndAddOutputFileTypeComboBox();
		createAndAddScenesNumberField();
		createAndAddStartEndFrameNumberField();

		createFileUploadPanel();

		createGridAndSubmitPanel();
		createStatusPanel(new BlenderJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
		addListeners();
	}

	private void createStatusSubmtionFieldSet() {
		statusSubmitionFieldSet = new FieldSet();  
		statusSubmitionFieldSet.setHeading("Submission Status");  
		statusSubmitionFieldSet.setCollapsible(false);  
		statusSubmitionFieldSet.setWidth(670);
		statusSubmitionFieldSet.setBorders(true);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(200);
		statusSubmitionFieldSet.setLayout(layout);
		mainPanel.add(statusSubmitionFieldSet);
	}

	private void createSubmitionFieldSet() {
		inputSubmitionFieldSet = new FieldSet();  
		inputSubmitionFieldSet.setHeading("Inputs Submition");  
		inputSubmitionFieldSet.setCollapsible(false);
		inputSubmitionFieldSet.setWidth(670);
		inputSubmitionFieldSet.setBorders(true);
		inputSubmitionFieldSet.setToolTip("Blender file to render");

		FormLayout layout = new FormLayout(); 
		layout.setLabelWidth(200);

		inputSubmitionFieldSet.setLayout(layout);
		mainPanel.add(inputSubmitionFieldSet);
	}

	private void createGridAndSubmitPanel() {
		gridAndSubmitPanel = new HorizontalPanel();
		gridAndSubmitPanel.setSpacing(10);
		gridAndSubmitPanel.setWidth(675);
		createAndAddFileGrid();
		createSubmitButton();
		statusSubmitionFieldSet.add(gridAndSubmitPanel);
	}

	private void createFileUploadPanel() {
		fileUploadPanel = new VerticalPanel();
		fileUploadPanel.setSpacing(10);
		createAndAddEmailNotificationCheckBox();
		createAndAddFileSubmissionPanel();
		inputSubmitionFieldSet.add(fileUploadPanel);
	}

	private void createAndAddFileGrid() {
		createGrid();

		this.store = new GroupingStore<InputFileGridModel>();  

		grid = new Grid<InputFileGridModel>(this.store, this.columnModel);
		grid.setStyleAttribute("borderTop", "none");  
		grid.setHeight(100);
		grid.setBorders(true);  
		grid.setStripeRows(true);

		grid.setSelectionModel(sm);  
		grid.addPlugin(sm); 

		gridAndSubmitPanel.add(grid);  		
	}

	private void createGrid() {

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();  

		sm = new CheckBoxSelectionModel<InputFileGridModel>();  

		configs.add(sm.getColumn());  

		ColumnConfig column = new ColumnConfig();
		column.setId("name");  
		column.setHeader("Input Name");  
		column.setWidth(150);  
		configs.add(column);  

		column = new ColumnConfig();  
		column.setId("output");  
		column.setHeader("Output Type");  
		column.setWidth(75);  
		configs.add(column);

		column = new ColumnConfig();  
		column.setId("scenes");  
		column.setHeader("Scenes Number");  
		column.setWidth(100);  
		configs.add(column);  

		column = new ColumnConfig();  
		column.setId("start");  
		column.setHeader("Start Frame");  
		column.setWidth(75);  
		configs.add(column);  

		column = new ColumnConfig();  
		column.setId("end");  
		column.setHeader("End Frame");  
		column.setWidth(75);  
		configs.add(column);  

		this.columnModel = new ColumnModel(configs);		
	}

	private void createAndAddStartEndFrameNumberField() {

		startFrameNumberField = new TextField<String>();
		startFrameNumberField.setId("startFrameNumberIdBlenderJobs-" + this.getJobViewId());
		startFrameNumberField.setFieldLabel("Start frame number");
		startFrameNumberField.setEmptyText("Start frame number") ;
		startFrameNumberField.setAllowBlank(false);
		startFrameNumberField.setValidator(new VTypeValidator(VType.NUMERIC));
		startFrameNumberField.setToolTip("Render from that frame");

		inputSubmitionFieldSet.add(startFrameNumberField);

		endFrameNumberField = new TextField<String>();
		endFrameNumberField.setId("endFrameNumberIdBlenderJobs-" + this.getJobViewId());
		endFrameNumberField.setFieldLabel("End frame number");
		endFrameNumberField.setEmptyText("End frame number") ;
		endFrameNumberField.setAllowBlank(false);
		endFrameNumberField.setValidator(new VTypeValidator(VType.NUMERIC));
		endFrameNumberField.setToolTip("Render until that frame");

		inputSubmitionFieldSet.add(endFrameNumberField);

	}


	private void createAndAddScenesNumberField() {
		scenesNumberField = new TextField<String>();
		scenesNumberField.setId("scenesNumberIdBlenderJobs-" + this.getJobViewId());
		scenesNumberField.setFieldLabel("Scenes number");
		scenesNumberField.setEmptyText("Scenes number") ;
		scenesNumberField.setAllowBlank(false);
		scenesNumberField.setValidator(new VTypeValidator(VType.NUMERIC));
		inputSubmitionFieldSet.add(scenesNumberField);
	}

	private void createAndAddOutputFileTypeComboBox() {

		ListStore<OutputTypeFileComboBoxModel> store = new ListStore<OutputTypeFileComboBoxModel>();  
		store.add(createOutputTypeFileComboBoxModelStore());

		outputTypeFile = new ComboBox<OutputTypeFileComboBoxModel>();
		outputTypeFile.setWidth(250);

		outputTypeFile.setAllowBlank(true);
		outputTypeFile.setForceSelection(true);
		outputTypeFile.setTypeAhead(true);
		outputTypeFile.setSelectOnFocus(true);

		outputTypeFile.setFieldLabel("Output type selection");  
		outputTypeFile.setDisplayField("type");  
		outputTypeFile.setName("type");  
		outputTypeFile.setValueField("type");
		outputTypeFile.setEmptyText("Select the output type");
		outputTypeFile.setStore(store);  
		outputTypeFile.setId("outputTypeFileIdBlenderJobs-" + this.getJobViewId());
		outputTypeFile.setTriggerAction(TriggerAction.ALL); 

		outputTypeFile.setToolTip("Output extension");

		inputSubmitionFieldSet.add(outputTypeFile);

	}
	private List<OutputTypeFileComboBoxModel> createOutputTypeFileComboBoxModelStore() {

		List<OutputTypeFileComboBoxModel> store = new ArrayList<OutputTypeFileComboBoxModel>();  
		outputTypeMap = BlenderPropertiesUtil.getBlenderPropertiesOutputTypeFile();

		for (String typeFile : outputTypeMap.keySet()) {
			store.add(new OutputTypeFileComboBoxModel(typeFile));
		}
		return store;
	}

	private void createAndAddFileSubmissionPanel() {

		fileSubmissionPanel = new FormPanel();
		fileSubmissionPanel.setHeaderVisible(false);
		fileSubmissionPanel.setBorders(false);
		fileSubmissionPanel.setBodyBorder(false);

		fileUploadField = new OurFileUploadField();
		LayoutContainer container = new LayoutContainer();
		container.setLayout(new RowLayout());
		container.add(fileUploadField);

		fileUploadField.addListener(new OurFileUploadEventListener() {
			public void onEvent(Event event) {
				if(validateFields()){
					maskMainPanel("Loading...");
					requestFileUpload();
					unmaskMainPanel();
				}
			}

		});

		fileSubmissionPanel.add(container);
		fileUploadPanel.add(fileSubmissionPanel);

	}

	protected boolean validateFields() {

		if (outputTypeFile.getValue() == null || outputTypeFile.getValue().getOutputTypeFile().length() == 0 || !outputTypeMap.containsKey(outputTypeFile.getValue().getOutputTypeFile().toUpperCase())) {
			MessageBox.alert("Output Type File Failed", "The output type file is not valid.", null);
			return false;
		}

		if (!scenesNumberField.isValid() || (Integer.valueOf(scenesNumberField.getValue()) <= 0)) {
			MessageBox.alert("Scenes Number Failed", "The scenes number is not valid.", null);
			return false;
		}

		if (!startFrameNumberField.isValid() || (Integer.valueOf(startFrameNumberField.getValue()) <= 0)) {
			MessageBox.alert("Start Frame Number Failed", "The start frame number is not valid.", null);
			return false;
		}

		if (!endFrameNumberField.isValid() || Integer.valueOf(endFrameNumberField.getValue()) < Integer.valueOf(startFrameNumberField.getValue()) || Integer.valueOf(endFrameNumberField.getValue()) > 300000 ) {
			MessageBox.alert("End Frame Number Failed", "The end frame number is not valid.", null);
			return false;
		}

		return true;
	}

	private void createSubmitButton() {
		submitButton = new Button("Submit");
		submitButton.setId("submitButtonIdJobs");

		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
			public void componentSelected(ButtonEvent ce) { 
				submitButton.disable();
				GridSelectionModel<InputFileGridModel> gridSelectionModel = grid.getSelectionModel();
				if (!gridSelectionModel.getSelectedItems().isEmpty()) {
					submitJob(gridSelectionModel);
					getTabItem().setClosable(false);
				}else{
					MessageBox.alert("Submit Failed", "No file selected.", null);
					submitButton.enable();
				}
			}  
		});

		gridAndSubmitPanel.add(submitButton);
	}

	protected void desactivateInputsSubmitionFieldSet() {
		inputSubmitionFieldSet.setExpanded(false);
		inputSubmitionFieldSet.setVisible(false);
		doLayout();
	}

	protected void submitJob(GridSelectionModel<InputFileGridModel> gridSelectionModel) {

		UserModel userModel = OurGridPortal.getUserModel();

		BlenderJobSubmissionTO jobSubmissionTO = createBlenderJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin(), gridSelectionModel.getSelectedItems(), emailNotification.getValue());

		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<BlenderJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(BlenderJobSubmissionResponseTO result) {
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
		desactivateInputsSubmitionFieldSet();
		
		jobStatusPanel.setVisible(true);
		jobStatusFieldSet.setVisible(true);
		jobStatusPanel.scheduleJobDescriptionRepeatedAction();
		submitButton.setEnabled(false);		
	}

	private BlenderJobSubmissionTO createBlenderJobSubmissionTO(Long uploadSessionId, String userLogin,
			List<InputFileGridModel> listInput, Boolean emailNotification) {

		BlenderJobSubmissionTO blenderJobSubmissionTO = new BlenderJobSubmissionTO();
		blenderJobSubmissionTO.setExecutorName(CommonServiceConstants.BLENDER_JOB_SUBMISSION_EXECUTOR);
		blenderJobSubmissionTO.setUploadId(uploadSessionId);
		blenderJobSubmissionTO.setUserLogin(userLogin);
		blenderJobSubmissionTO.setInputFileList(createListInputFileTO(listInput));
		blenderJobSubmissionTO.setJobName("Blender Job");
		blenderJobSubmissionTO.setEmailNotification(emailNotification);
		return blenderJobSubmissionTO;
	}

	private List<BlenderInputFileTO> createListInputFileTO(List<InputFileGridModel> listInput) {
		List<BlenderInputFileTO> list = new ArrayList<BlenderInputFileTO>();
		for (InputFileGridModel input : listInput) {

			BlenderInputFileTO inputFileTO = new BlenderInputFileTO();

			inputFileTO.setName(input.getName());
			inputFileTO.setUploadId(input.getUploadId());
			inputFileTO.setScenesNumber(input.getScenesNumber());
			inputFileTO.setOutputType(input.getOutputTypeFile());
			inputFileTO.setOutputExt(input.getOutputExtFile());
			inputFileTO.setStart(input.getStartFrame());
			inputFileTO.setEnd(input.getEndFrame());

			list.add(inputFileTO);
		}
		return list;
	}

	private void createAndAddEmailNotificationCheckBox() {
		HorizontalPanel emailNotificationPanel = new HorizontalPanel();

		emailNotification = new CheckBox();
		emailNotification.setBoxLabel("Email notification");

		emailNotificationPanel.add(emailNotification);
		inputSubmitionFieldSet.add(emailNotificationPanel);
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

	protected void maskMainPanel(String message) {
		this.el().mask(message);
	}

	protected void unmaskMainPanel() {
		this.el().unmask();
	}

	private void requestFileUpload() {

		if (!validUploadFileName()) {
			unmaskMainPanel();
			MessageBox.alert("Invalid Format", BlenderJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG, null);
			return;
		}

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
		fileUploadField.setName(uploadId.toString());

		String fileName = fileUploadField.getFilename();
		Long uploadNameId = uploadId;
		Integer scenesNumber = new Integer(scenesNumberField.getValue());
		String outputType = outputTypeFile.getValue().getOutputTypeFile();
		String outputExt = outputTypeMap.get(outputType);
		Integer startFrame = new Integer(startFrameNumberField.getValue());
		Integer endFrame = new Integer(endFrameNumberField.getValue());

		store.add(new InputFileGridModel(fileName, uploadNameId, scenesNumber, outputType, outputExt, startFrame, endFrame));
		refresh();

		String formAction = CommonServiceConstants.UPLOAD_FORM_ACTION + 
		CommonServiceConstants.USER_NAME_PARAMETER + "=" + userModel.getUserLogin() + "&" +
		CommonServiceConstants.TO_DESCOMPACT + "=false&" +
		CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=true&" +
		CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId;

		fileSubmissionPanel.setAction(formAction);
		fileSubmissionPanel.setMethod(Method.POST);
		fileSubmissionPanel.setEncoding(Encoding.MULTIPART);
		fileSubmissionPanel.submit();
	}

	public void updateGridPanel(List<BlenderInputFileTO> inputs) {
		for (BlenderInputFileTO input : inputs) {
			store.add(new InputFileGridModel(input.getName(), input.getUploadId(),
					input.getScenesNumber(), input.getOutputType(), input.getOutputExt(), input.getStart(), input.getEnd()));	
		}
		refresh();
	}

	private boolean validUploadFileName() {

		String[] validExtensions = new String[] {"blend"};

		for (String validExtension : validExtensions) {
			if (fileUploadField.getFilename().endsWith(validExtension)) {
				return true;
			}
		}

		return false;
	}

	public String getOutputType() {
		return outputTypeFile.getValueField();
	}

	public String getScenesNumber() {
		return scenesNumberField.getValue();
	}

	public void desactivateSubmission() {
		outputTypeFile.setEnabled(false);
		scenesNumberField.setEnabled(false);
		startFrameNumberField.setEnabled(false);
		endFrameNumberField.setEnabled(false);
		emailNotification.setEnabled(false);
		fileUploadPanel.setEnabled(false);
		submitButton.setEnabled(false);
	}

	public void activateSubmission() {
		outputTypeFile.setEnabled(true);
		scenesNumberField.setEnabled(true);
		startFrameNumberField.setEnabled(true);
		endFrameNumberField.setEnabled(true);
		emailNotification.setEnabled(true);
		fileUploadPanel.setEnabled(true);
		submitButton.setEnabled(true);
	}

	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();
		List<BlenderInputFileTO> blenderInputFiles = new LinkedList<BlenderInputFileTO>();
		for (Object object : inputs) {
			BlenderInputFileTO inputFile = (BlenderInputFileTO) object;
			blenderInputFiles.add(inputFile);
		}
		updateGridPanel(blenderInputFiles);
		
	}
}