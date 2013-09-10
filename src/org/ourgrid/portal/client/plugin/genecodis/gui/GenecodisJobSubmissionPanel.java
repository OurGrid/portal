package org.ourgrid.portal.client.plugin.genecodis.gui;

import java.util.ArrayList;
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
import org.ourgrid.portal.client.plugin.genecodis.gui.model.AnalysisTypeComboBoxModel;
import org.ourgrid.portal.client.plugin.genecodis.gui.model.InputFileGridModel;
import org.ourgrid.portal.client.plugin.genecodis.gui.model.StatisticalTestTypeComboBoxModel;
import org.ourgrid.portal.client.plugin.genecodis.gui.util.GenecodisJobSubmissionMessages;
import org.ourgrid.portal.client.plugin.genecodis.gui.util.GenecodisPropertiesUtil;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;
import org.ourgrid.portal.client.plugin.genecodis.to.response.GenecodisJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.genecodis.to.response.GenecodisSendInputResponseTO;
import org.ourgrid.portal.client.plugin.genecodis.to.service.GenecodisJobSubmissionTO;
import org.ourgrid.portal.client.plugin.genecodis.to.service.GenecodisSendInputTO;
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
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
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GenecodisJobSubmissionPanel extends PluginJobSubmissionPanel {

	private Grid<InputFileGridModel> grid;

	private GroupingStore<InputFileGridModel> store;

	private ColumnModel columnModel;

	private FormPanel fileSubmissionPanel;

	private ComboBox<AnalysisTypeComboBoxModel> analysisType;

	private ComboBox<StatisticalTestTypeComboBoxModel> statisticalTestType;

	private TextField<String> supportNumberField;

	private TextField<String> supportForRandomNumberField;

	private TextField<String> referenceSizeField;

	private TextField<String> selectedReferenceSizeField;

	private TextField<String> pValueField;

	private OurFileUploadField fileUploadField;

	private CheckBox emailNotificationCheckBox;

	private VerticalPanel fileUploadPanel;

	private HorizontalPanel gridAndSubmitPanel;

	private FieldSet gridFieldSet;

	private FieldSet inputSubmitionFieldSet;

	private Button submitButton;

	private Map<String, Integer> analysisTypeMap;

	private Map<String, Integer> statisticalTestTypeMap;

	private CheckBox allPossibilities;

	private Button sendButton;

	private VerticalPanel sendButtonPanel;

	private Long uploadId;
	private String inputFileName;

	private CheckBoxSelectionModel<InputFileGridModel> sm;

	public GenecodisJobSubmissionPanel(Integer jobTabCount, Integer jobViewId,
			TabItem container) {

		super(jobTabCount, jobViewId, container);
	}

	// LAYOUT
	@Override
	protected void init() {

		createMainPanel();

		createSubmitionFieldSet();

		createFileUploadPanel();

		createAndAddAllPossibilitiesPanelCheckBox();

		createAndAddSupportsFields();
		createAndAddAnalysisTypeComboBox();
		createAndAddReferencesFields();
		createAndAddStatisticalTestTypeComboBox();
		createAndAddPValueField();
		createAndAddEmailNotificationCheckBox();
		createAndAddSendButtonPanel();

		createGridFieldSet();
		createGridAndSubmitPanel();
		createStatusFieldSet("Genecodis Status");
		createStatusPanel(new GenecodisJobStatusPanel(getJobViewId(), getJobTabCount(),
				getTabItem()));

	}

	private void createGridFieldSet() {
		gridFieldSet = new FieldSet();
		gridFieldSet.setHeading("Grid Submition");
		gridFieldSet.setCollapsible(true);
		gridFieldSet.setWidth(740);
		gridFieldSet.setBorders(true);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(200);

		gridFieldSet.setLayout(layout);
		mainPanel.add(gridFieldSet);
	}

	private void createSubmitionFieldSet() {
		inputSubmitionFieldSet = new FieldSet();
		inputSubmitionFieldSet.setHeading("Inputs Submition");
		inputSubmitionFieldSet.setCollapsible(true);
		inputSubmitionFieldSet.setWidth(740);
		inputSubmitionFieldSet.setBorders(true);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(200);

		inputSubmitionFieldSet.setLayout(layout);
		mainPanel.add(inputSubmitionFieldSet);
	}

	private void createGridAndSubmitPanel() {
		gridAndSubmitPanel = new HorizontalPanel();
		gridAndSubmitPanel.setSpacing(10);
		gridAndSubmitPanel.setWidth(740);
		createAndAddFileGrid();
		createSubmitButton();
		gridFieldSet.add(gridAndSubmitPanel);
	}

	private void createFileUploadPanel() {
		fileUploadPanel = new VerticalPanel();
		fileUploadPanel.setSpacing(10);
		createAndAddFileSubmissionPanel();
		inputSubmitionFieldSet.add(fileUploadPanel);
	}

	private void createAndAddSendButtonPanel() {

		sendButtonPanel = new VerticalPanel();
		sendButtonPanel.setSpacing(10);

		sendButton = new Button("Send");
		sendButton.setBorders(false);

		sendButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				if (validateFields()) {
					maskMainPanel("Loading...");
					sendInput();
					unmaskMainPanel();
				}
			}
		});

		sendButtonPanel.add(sendButton);
		inputSubmitionFieldSet.add(sendButtonPanel);
	}

	private void sendInput() {

		ServiceTO serviceTO = createGenecodisSendInputTO();
		OurGridPortalServerUtil.getInstance().execute(serviceTO,
				new AsyncCallback<GenecodisSendInputResponseTO>() {

			public void onSuccess(GenecodisSendInputResponseTO result) {
				sendInputedFile(result.getInputFile());
				showInputGrid();
			}

			public void onFailure(Throwable caught) {
				unmaskMainPanel();
				MessageBox.alert("Send Input Error", caught
						.getMessage(), null);
			}

		});
	}

	protected void showInputGrid() {
		modifyGrid();
		desactivateInputsSubmitionFieldSet();
	}

	private void sendInputedFile(List<GenecodisInputFileTO> list) {

		for (GenecodisInputFileTO genecodisInputFileTO : list) {

			InputFileGridModel inputFileGridModel = new InputFileGridModel(
					this.inputFileName, this.uploadId, genecodisInputFileTO
					.getGenesSupport(), genecodisInputFileTO
					.getAnalysisType(), genecodisInputFileTO
					.getSupportForRandom(), genecodisInputFileTO
					.getReferenceSize(), genecodisInputFileTO
					.getSelectedRefSize(), genecodisInputFileTO
					.getStatisticalTest(), genecodisInputFileTO
					.getCorrectionMethod());

			inputFileGridModel.setGenecodisInputFileTO(genecodisInputFileTO);
			store.add(inputFileGridModel);
		}
		refresh();
	}

	private GenecodisSendInputTO createGenecodisSendInputTO() {

		GenecodisSendInputTO genecodisSendInputTO = new GenecodisSendInputTO();

		genecodisSendInputTO
		.setExecutorName(CommonServiceConstants.GENECODIS_SEND_INPUT_EXECUTOR);
		genecodisSendInputTO.setAllParameters(allPossibilities.getValue());
		genecodisSendInputTO.setInputFileName(inputFileName);

		if (!allPossibilities.getValue()) {
			genecodisSendInputTO.setAnalysisType(analysisType.getValue()
					.getAnalysisValue());
			genecodisSendInputTO.setCorrectionMethod(new Integer(pValueField
					.getValue()));
			genecodisSendInputTO.setGenesSupport(new Integer(supportNumberField
					.getValue()));
			genecodisSendInputTO.setStatisticalTest(statisticalTestType
					.getValue().getStatisticalTestValue());
			genecodisSendInputTO.setSupportForRandom(new Integer(
					supportForRandomNumberField.getValue()));
		}

		genecodisSendInputTO.setReferenceSize(new Integer(referenceSizeField
				.getValue()));
		genecodisSendInputTO.setSelectedRefSize(new Integer(
				selectedReferenceSizeField.getValue()));

		return genecodisSendInputTO;
	}

	private void requestFileUpload() {

		if (!validUploadFileName()) {
			unmaskMainPanel();
			MessageBox.alert("Invalid Format",
					GenecodisJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG,
					null);
			return;
		}

		UserModel userModel = OurGridPortal.getUserModel();
		ServiceTO serviceTO = createUploadSessionIdTO(userModel.getUserLogin());
		OurGridPortalServerUtil.getInstance().execute(serviceTO,
				new AsyncCallback<UploadSessionIDResponseTO>() {

			public void onSuccess(UploadSessionIDResponseTO result) {
				sendUploadedFile(result.getUploadId());
			}

			public void onFailure(Throwable caught) {
				unmaskMainPanel();
				MessageBox.alert("Upload Error", caught.getMessage(),
						null);
			}

		});

	}

	private void createAndAddSupportField() {
		supportNumberField = new TextField<String>();
		supportNumberField.setFieldLabel("Support");
		supportNumberField
		.setId("supportNumberFieldIdJobs-" + this.getJobTabCount());
		supportNumberField.setEmptyText("Suport");
		supportNumberField.setAllowBlank(false);
		supportNumberField.setValidator(new VTypeValidator(VType.NUMERIC));
		supportNumberField
		.setToolTip("Minimum number of genes required. Default: 3");
		inputSubmitionFieldSet.add(supportNumberField);
	}

	private void createAndAddFileGrid() {
		createGrid();

		this.store = new GroupingStore<InputFileGridModel>();

		grid = new Grid<InputFileGridModel>(this.store, this.columnModel);
		grid.setStyleAttribute("borderTop", "none");
		grid.setHeight(60);
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
		column.setId("analysis type");
		column.setHeader("Analysis Type");
		column.setWidth(80);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("support for random");
		column.setHeader("Support for Random");
		column.setWidth(75);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("reference size");
		column.setHeader("Reference Size");
		column.setWidth(75);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("selected reference size");
		column.setHeader("Selected Reference Size");
		column.setWidth(75);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("statistical test");
		column.setHeader("Statistical Test");
		column.setWidth(75);
		configs.add(column);

		column = new ColumnConfig();
		column.setId("p-value");
		column.setHeader("P-Value");
		column.setWidth(60);
		configs.add(column);

		this.columnModel = new ColumnModel(configs);
	}

	private void createAndAddSupportForRandomNumberField() {
		supportForRandomNumberField = new TextField<String>();
		supportForRandomNumberField.setFieldLabel("Support for random number");
		supportForRandomNumberField.setEmptyText("Support for random number");
		supportForRandomNumberField.setId("supportForRandomIdJobs-"
				+ this.getJobTabCount());
		supportForRandomNumberField.setAllowBlank(false);
		supportForRandomNumberField.setValidator(new VTypeValidator(
				VType.NUMERIC));
		supportForRandomNumberField
		.setToolTip("Minimum support taked when the algorithm is correcting p-values . Usually the same than Support.");
		inputSubmitionFieldSet.add(supportForRandomNumberField);
	}

	private void createAndAddSupportsFields() {
		createAndAddSupportField();
		createAndAddSupportForRandomNumberField();
	}

	private void createAndAddReferencesFields() {
		referenceSizeField = new TextField<String>();
		referenceSizeField.setFieldLabel("Reference size");
		referenceSizeField.setEmptyText("Reference size");
		referenceSizeField.setId("referenceSizeIdJobs-" + this.getJobTabCount());
		referenceSizeField.setAllowBlank(false);
		referenceSizeField.setValidator(new VTypeValidator(VType.NUMERIC));
		referenceSizeField
		.setToolTip("Total number of transactions in the input file.");

		inputSubmitionFieldSet.add(referenceSizeField);

		selectedReferenceSizeField = new TextField<String>();
		selectedReferenceSizeField.setFieldLabel("Selected reference size");
		selectedReferenceSizeField.setId("selectedRefSizeIdJobs-"
				+ this.getJobTabCount());
		selectedReferenceSizeField.setEmptyText("Selected reference size");
		selectedReferenceSizeField.setAllowBlank(false);
		selectedReferenceSizeField.setValidator(new VTypeValidator(
				VType.NUMERIC));
		selectedReferenceSizeField
		.setToolTip("Number of transacions selected in the input file.");

		inputSubmitionFieldSet.add(selectedReferenceSizeField);
	}

	private void createAndAddStatisticalTestTypeComboBox() {
		ListStore<StatisticalTestTypeComboBoxModel> store = new ListStore<StatisticalTestTypeComboBoxModel>();
		store.add(createStatisticalTestTypeComboBoxModelStore());

		statisticalTestType = new ComboBox<StatisticalTestTypeComboBoxModel>();
		statisticalTestType.setWidth(100);

		statisticalTestType.setAllowBlank(true);
		statisticalTestType.setForceSelection(true);
		statisticalTestType.setTypeAhead(true);
		statisticalTestType.setSelectOnFocus(true);

		statisticalTestType.setFieldLabel("Statistical test type selection");
		statisticalTestType.setDisplayField("statisticalTestType");
		statisticalTestType.setName("statisticalTestType");
		statisticalTestType.setValueField("statisticalTestType");
		statisticalTestType.setEmptyText("Select the statistical test type");
		statisticalTestType.setStore(store);
		statisticalTestType.setId("statisticalTestTypeFileIdJobs-"
				+ this.getJobTabCount());
		statisticalTestType.setTriggerAction(TriggerAction.ALL);

		inputSubmitionFieldSet.add(statisticalTestType);
	}

	private List<StatisticalTestTypeComboBoxModel> createStatisticalTestTypeComboBoxModelStore() {
		List<StatisticalTestTypeComboBoxModel> store = new ArrayList<StatisticalTestTypeComboBoxModel>();
		statisticalTestTypeMap = GenecodisPropertiesUtil
		.getGenecodisPropertiesStatisticalTestType();

		for (String typeFile : statisticalTestTypeMap.keySet()) {
			store.add(new StatisticalTestTypeComboBoxModel(typeFile,
					statisticalTestTypeMap.get(typeFile)));
		}
		return store;
	}

	private void createAndAddPValueField() {
		pValueField = new TextField<String>();
		pValueField.setFieldLabel("P-Value");
		pValueField.setEmptyText("P-Value");
		pValueField.setId("pValueIdJobs-" + this.getJobTabCount());
		pValueField.setAllowBlank(false);
		pValueField.setValidator(new VTypeValidator(VType.NUMERIC));
		pValueField.setToolTip("Number of permutations to correct p-values.");
		inputSubmitionFieldSet.add(pValueField);

	}

	private void createAndAddAnalysisTypeComboBox() {

		ListStore<AnalysisTypeComboBoxModel> store = new ListStore<AnalysisTypeComboBoxModel>();
		store.add(createAnalysisTypeComboBoxModelStore());

		analysisType = new ComboBox<AnalysisTypeComboBoxModel>();
		analysisType.setWidth(100);

		analysisType.setAllowBlank(true);
		analysisType.setForceSelection(true);
		analysisType.setTypeAhead(true);
		analysisType.setSelectOnFocus(true);

		analysisType.setFieldLabel("Analysis type selection");
		analysisType.setDisplayField("analysisType");
		analysisType.setName("analysisType");
		analysisType.setValueField("analysisType");
		analysisType.setEmptyText("Select the analysis type");
		analysisType.setStore(store);
		analysisType.setId("analysisTypeFileIdJobs-" + this.getJobTabCount());
		analysisType.setTriggerAction(TriggerAction.ALL);

		inputSubmitionFieldSet.add(analysisType);

	}

	private List<AnalysisTypeComboBoxModel> createAnalysisTypeComboBoxModelStore() {

		List<AnalysisTypeComboBoxModel> store = new ArrayList<AnalysisTypeComboBoxModel>();
		analysisTypeMap = GenecodisPropertiesUtil
		.getGenecodisPropertiesAnalysisType();

		for (String typeFile : analysisTypeMap.keySet()) {
			store.add(new AnalysisTypeComboBoxModel(typeFile, analysisTypeMap
					.get(typeFile)));
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
				maskMainPanel("Loading...");
				requestFileUpload();
				unmaskMainPanel();
			}

		});

		fileSubmissionPanel.add(container);
		fileUploadPanel.add(fileSubmissionPanel);

	}

	protected boolean validateFields() {

		if (!allPossibilities.getValue()
				&& (analysisType.getValue().getAnalysisType().length() == 0 || !analysisTypeMap
						.containsKey(analysisType.getValue().getAnalysisType()
								.toUpperCase()))) {
			MessageBox.alert("Analysis Type Failed",
					"The analysis type is not valid.", null);
			return false;
		}

		if (!allPossibilities.getValue()
				&& (!supportNumberField.isValid() || (Integer
						.valueOf(supportNumberField.getValue()) <= 0))) {
			MessageBox.alert("Support number Failed",
					"The support number is not valid.", null);
			return false;
		}

		if (!allPossibilities.getValue()
				&& (!supportForRandomNumberField.isValid() || (Integer
						.valueOf(supportForRandomNumberField.getValue()) <= 0))) {
			MessageBox.alert("Support for Random Number Failed",
					"The support for random number is not valid.", null);
			return false;
		}

		if (!referenceSizeField.isValid()
				|| (Integer.valueOf(referenceSizeField.getValue()) <= 0)) {
			MessageBox.alert("Reference Size Number Failed",
					"The reference size number is not valid.", null);
			return false;
		}

		if (!selectedReferenceSizeField.isValid()
				|| (Integer.valueOf(selectedReferenceSizeField.getValue()) <= 0)) {
			MessageBox.alert("Selected Reference Size Number Failed",
					"The selected reference size number is not valid.", null);
			return false;
		}

		if (!allPossibilities.getValue()
				&& (statisticalTestType.getValue().getStatisticalTestType()
						.length() == 0 || !statisticalTestTypeMap
						.containsKey(statisticalTestType.getValue()
								.getStatisticalTestType().toUpperCase()))) {
			MessageBox.alert("Statistical Test Type Failed",
					"The statistical test type is not valid.", null);
			return false;
		}

		if (!allPossibilities.getValue() && !pValueField.isValid()) {
			MessageBox.alert("P-Value Number Failed",
					"The P-Value number is not valid.", null);
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
				GridSelectionModel<InputFileGridModel> gridSelectionModel = grid
				.getSelectionModel();
				if (!gridSelectionModel.getSelectedItems().isEmpty()) {
					submitJob(gridSelectionModel);
					// desactivateAllInputsFields();
					desactivateSendButton();
				} else {
					MessageBox
					.alert("Submit Failed", "No file selected.", null);
				}
			}

		});

		gridAndSubmitPanel.add(submitButton);
	}

	private void modifyGrid() {
		grid.setHeight(180);
	}

	private void desactivateInputsSubmitionFieldSet() {
		inputSubmitionFieldSet.setVisible(false);
	}

	private void desactivateSendButton() {
		sendButton.setEnabled(false);
	}

	protected void submitJob(
			GridSelectionModel<InputFileGridModel> gridSelectionModel) {

		UserModel userModel = OurGridPortal.getUserModel();

		GenecodisJobSubmissionTO jobSubmissionTO = createGenecodisJobSubmissionTO(
				userModel.getUploadSessionId(), userModel.getUserLogin(),
				gridSelectionModel.getSelectedItems(),
				emailNotificationCheckBox.getValue());

		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO,
				new AsyncCallback<GenecodisJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox
				.alert(
						"Submission Error",
						GenecodisJobSubmissionMessages.SUBMISSION_ERROR_MSG,
						null);
				submitButton.enable();
			}

			public void onSuccess(
					GenecodisJobSubmissionResponseTO result) {
				unmaskMainPanel();
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

	private GenecodisJobSubmissionTO createGenecodisJobSubmissionTO(
			Long uploadSessionId, String userLogin,
			List<InputFileGridModel> listInput, Boolean emailNotification) {

		GenecodisJobSubmissionTO genecodisJobSubmissionTO = new GenecodisJobSubmissionTO();
		genecodisJobSubmissionTO
		.setExecutorName(CommonServiceConstants.GENECODIS_JOB_SUBMISSION_EXECUTOR);
		genecodisJobSubmissionTO.setUploadId(uploadSessionId);
		genecodisJobSubmissionTO.setInputFileName(inputFileName);
		genecodisJobSubmissionTO.setUserLogin(userLogin);
		genecodisJobSubmissionTO.setInputFile(createListInputFileTO(listInput));
		genecodisJobSubmissionTO.setJobName("Genecodis Job");
		genecodisJobSubmissionTO.setEmailNotification(emailNotification);
		return genecodisJobSubmissionTO;
	}

	private List<GenecodisInputFileTO> createListInputFileTO(
			List<InputFileGridModel> listInput) {
		List<GenecodisInputFileTO> list = new ArrayList<GenecodisInputFileTO>();
		for (InputFileGridModel input : listInput) {

			input.getGenecodisInputFileTO();
			list.add(input.getGenecodisInputFileTO());
		}
		return list;
	}

	private void createAndAddAllPossibilitiesPanelCheckBox() {
		HorizontalPanel allPossibilitiesPanel = new HorizontalPanel();

		allPossibilities = new CheckBox();
		allPossibilities.setBoxLabel("Generate all possibilities");

		allPossibilities.addListener(Events.OnClick,
				new Listener<FieldEvent>() {

			@Override
			public void handleEvent(FieldEvent be) {
				if (allPossibilities.getValue()) {
					supportNumberField.disable();
					supportForRandomNumberField.disable();
					analysisType.disable();
					statisticalTestType.disable();
					pValueField.disable();

				} else {
					supportNumberField.enable();
					supportForRandomNumberField.enable();
					analysisType.enable();
					statisticalTestType.enable();
					pValueField.enable();
				}
			}
		});

		allPossibilitiesPanel.add(allPossibilities);
		inputSubmitionFieldSet.add(allPossibilitiesPanel);
	}

	private void createAndAddEmailNotificationCheckBox() {
		HorizontalPanel emailNotificationPanel = new HorizontalPanel();

		emailNotificationCheckBox = new CheckBox();
		emailNotificationCheckBox.setBoxLabel("Email notification");

		emailNotificationPanel.add(emailNotificationCheckBox);
		inputSubmitionFieldSet.add(emailNotificationPanel);
	}

	// OPERATIONS
	private void addListeners() {
		fileSubmissionPanel.addListener(Events.Submit,
				new Listener<FormEvent>() {

			public void handleEvent(FormEvent be) {

				if (isQuotaExceed(be.getResultHtml())) {
					MessageBox.alert("Disk quota exceeded", "The specified file exceeds your quota.", null);
				}else{
					OurGridPortal.refreshFileExplorerRoot();
				}
			}
		});
	}

	protected boolean isQuotaExceed(String resultHtml) {
		return resultHtml
		.contains(CommonServiceConstants.QUOTA_EXCEED_ERROR_CODE);
	}

	protected void maskMainPanel(String message) {
		this.el().mask(message);
	}

	protected void unmaskMainPanel() {
		this.el().unmask();
	}

	private ServiceTO createUploadSessionIdTO(String userLogin) {

		UploadSessionIDTO uploadSessionIdTO = new UploadSessionIDTO();
		uploadSessionIdTO
		.setExecutorName(CommonServiceConstants.UPLOAD_SESSION_ID_EXECUTOR);
		uploadSessionIdTO.setUserLogin(userLogin);

		return uploadSessionIdTO;
	}

	private void sendUploadedFile(Long uploadId) {

		this.uploadId = uploadId;
		this.inputFileName = fileUploadField.getFilename();

		UserModel userModel = OurGridPortal.getUserModel();
		userModel.setUploadSessionId(uploadId);
		fileUploadField.setName(uploadId.toString());

		String formAction = CommonServiceConstants.UPLOAD_FORM_ACTION
		+ CommonServiceConstants.USER_NAME_PARAMETER + "="
		+ userModel.getUserLogin() + "&"
		+ CommonServiceConstants.TO_DESCOMPACT + "=false&"
		+ CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=true&"
		+ CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId;

		fileSubmissionPanel.setAction(formAction);
		fileSubmissionPanel.setMethod(Method.POST);
		fileSubmissionPanel.setEncoding(Encoding.MULTIPART);
		fileSubmissionPanel.submit();

	}

	private boolean validUploadFileName() {

		String[] validExtensions = new String[] { "engene" };

		for (String validExtension : validExtensions) {
			if (fileUploadField.getFilename().endsWith(validExtension)) {
				return true;
			}
		}

		return false;
	}

	public void showJobStatusTree() {
		gridFieldSet.setExpanded(false);
		submitButton.disable();

		showInputGrid();
		jobStatusFieldSet.setVisible(true);
		jobStatusPanel.setVisible(true);
		jobStatusPanel.scheduleJobDescriptionRepeatedAction();
		submitButton.disable();
	}

	public void updateGridPanel(List<GenecodisInputFileTO> inputs) {
		for (GenecodisInputFileTO input : inputs) {

			InputFileGridModel inputFileGridModel = new InputFileGridModel(
					input.getInputFilename(), 0L, input.getGenesSupport(),
					input.getAnalysisType(), input.getSupportForRandom(), input
					.getReferenceSize(), input.getSelectedRefSize(),
					input.getStatisticalTest(), input.getCorrectionMethod());

			store.add(inputFileGridModel);
		}
		refresh();
	}

	public void desactivateSubmission() {
		fileUploadPanel.setEnabled(false);
		allPossibilities.setEnabled(false);
		supportNumberField.setEnabled(false);
		supportForRandomNumberField.setEnabled(false);
		analysisType.setEnabled(false);
		referenceSizeField.setEnabled(false);
		selectedReferenceSizeField.setEnabled(false);
		statisticalTestType.setEnabled(false);
		pValueField.setEnabled(false);
		emailNotificationCheckBox.setEnabled(false);
		sendButton.setEnabled(false);
		submitButton.setEnabled(false);
	}

	public void activateSubmission() {
		fileUploadPanel.setEnabled(true);
		allPossibilities.setEnabled(true);
		supportNumberField.setEnabled(true);
		supportForRandomNumberField.setEnabled(true);
		analysisType.setEnabled(true);
		referenceSizeField.setEnabled(true);
		selectedReferenceSizeField.setEnabled(true);
		statisticalTestType.setEnabled(true);
		pValueField.setEnabled(true);
		emailNotificationCheckBox.setEnabled(true);
		sendButton.setEnabled(true);
		submitButton.setEnabled(true);
	}

	public void stopGetStatusAction() {
		jobStatusPanel.stopStatusTimer();
	}

	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();		
	}

}