package org.ourgrid.portal.client.plugin.reservatorios.gui;

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
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;
import org.ourgrid.portal.client.plugin.reservatorios.gui.model.SimulationPeriodComboBox;
import org.ourgrid.portal.client.plugin.reservatorios.to.model.ReservatoriosInputFileTO;
import org.ourgrid.portal.client.plugin.reservatorios.to.response.ReservatoriosJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.reservatorios.to.service.ReservatoriosJobSubmissionTO;
import org.ourgrid.portal.client.plugin.reservatorios.util.ReservatoriosJobSubmissionMessages;
import org.ourgrid.portal.client.plugin.reservatorios.util.ReservatoriosPropertiesUtil;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReservatoriosJobSubmissionPanel extends PluginJobSubmissionPanel {
	
	private FieldSet						      mainFieldSet;
	private FieldSet 							  typeSelectFieldSet;	
	private FieldSet 							  inputFilesFieldSet;
	private FieldSet 							  reservatorioFileFieldSet;
	private FieldSet 							  volumesFileFieldSet;
	private FieldSet 							  scenarioFileFieldSet;
	private FieldSet 							  climatePrevisionFieldSet;
	private FieldSet							  simulationPeriodFieldSet;
	
	private FormPanel							  fileSubmissionPanel;
	
	private OurFileUploadField 					  reservatorioFileUploadField;
	private OurFileUploadField 					  volumesFileUploadField;
	private OurFileUploadField 					  scenarioFileUploadField;
	
	private Radio 								  runCompleteButton;
	private Radio 								  runScenarioButton;
	private RadioGroup 							  runGroup;
	
	private NumberField							  dryNumberField;
	private NumberField							  normalNumberField;
	private NumberField							  rainyNumberField;
	
	private ComboBox<SimulationPeriodComboBox>	  initialPeriod;
	private ComboBox<SimulationPeriodComboBox>    finalPeriod;
	
	private Map<Integer, String> 				  initialPeriodMap;
	private Map<Integer, String> 				  finalPeriodMap;
	
	private Button								  submitButton;

	public ReservatoriosJobSubmissionPanel(Integer jobTabCounter, int jobViewId, TabItem container) {
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
		
		createClimatePrevisionFieldSet();
		createClimatePrevisionNumberFields();
		
		createSimulationPeriodFieldSet();
		createSimulationPeriodComboBoxes();
		
		createSubmitButton();
		
		createStatusFieldSet("Reservoir Status");
		createStatusPanel(new ReservatoriosJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
		
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
	
	private void createSimulationPeriodComboBoxes() {
		setInitialPeriod(createSimulationPeriodComboBox(initialPeriod, "Initial Period",initialPeriodMap));
		setFinalPeriod(createSimulationPeriodComboBox(finalPeriod, "Final Period",finalPeriodMap));
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
		
		runScenarioButton = new Radio();
		runScenarioButton.setBoxLabel("Scenario");
		runScenarioButton.setVisible(true);
		
		runGroup = new RadioGroup();
		runGroup.add(runCompleteButton);
		runGroup.add(runScenarioButton);
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
		
		container.add(getReservatorioFileFieldSet());
		container.add(getVolumesFileFieldSet());
		container.add(getScenarioFileFieldSet());

		fileSubmissionPanel.add(container);
		inputFilesFieldSet.add(fileSubmissionPanel);
		

	}

	
	
	private void createInputFilesSpots() {
		setInputFilesFieldSet();
		
		createInputFilesFieldSet(getReservatorioFileFieldSet(), "Reservoir File");
		createInputFilesFieldSet(getVolumesFileFieldSet(), "Volumes File");
		createInputFilesFieldSet(getScenarioFileFieldSet(), "Scenarios File");
		
		setInputFilesSpots();
		createReservatorioInputFilesSpots();
		createVolumesInputFilesSpots();
		createScenarioInputFilesSpots();
	}

	private void setInputFilesFieldSet() {
		setReservatorioFileFieldSet(new FieldSet());
		setScenarioFileFieldSet(new FieldSet());
		setVolumesFileFieldSet(new FieldSet());
	}
	
	private void createInputFilesFieldSet(FieldSet field, String heading) {
		field.setHeading(heading);
		field.setExpanded(true);
		field.setBorders(false);
		field.setWidth(720);
		
		inputFilesFieldSet.add(field);
	}
	
	private void createClimatePrevisionFieldSet() {
		climatePrevisionFieldSet = new FieldSet();
		climatePrevisionFieldSet.setHeading("Climate Prevision");
		climatePrevisionFieldSet.setExpanded(true);
		climatePrevisionFieldSet.setWidth(720);
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(200);
		
		climatePrevisionFieldSet.setLayout(layout);
		
		mainFieldSet.add(climatePrevisionFieldSet);
	}
	
	private void createSimulationPeriodFieldSet() {
		simulationPeriodFieldSet = new FieldSet();
		simulationPeriodFieldSet.setHeading("Simulation Period");
		simulationPeriodFieldSet.setExpanded(true);
		simulationPeriodFieldSet.setWidth(720);
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(200);
		simulationPeriodFieldSet.setLayout(layout);
		
		mainFieldSet.add(simulationPeriodFieldSet);
	}
	
	private ComboBox<SimulationPeriodComboBox> createSimulationPeriodComboBox(ComboBox<SimulationPeriodComboBox> comboBox, String period,Map<Integer, String> map) {
		ListStore<SimulationPeriodComboBox> store = new ListStore<SimulationPeriodComboBox>();  
		store.add(createSimulationPeriodComboBoxStore(map));

		comboBox = new ComboBox<SimulationPeriodComboBox>();
		comboBox.setWidth(250);

		comboBox.setAllowBlank(true);
		comboBox.setForceSelection(true);
		comboBox.setTypeAhead(true);
		comboBox.setSelectOnFocus(true);

		
		comboBox.setFieldLabel(period);  
		comboBox.setDisplayField("month");  
		comboBox.setValueField("month");
		comboBox.setName(period);  
		comboBox.setStore(store);  
		comboBox.setTriggerAction(TriggerAction.ALL); 
		comboBox.setAllowBlank(false);

		comboBox.setToolTip(period);

		simulationPeriodFieldSet.add(comboBox);
		return comboBox;
	}
	
	private void setInputFilesSpots() {
		setReservatorioFileUploadField(new OurFileUploadField());
		setVolumesFileUploadField(new OurFileUploadField());
		setScenarioFileUploadField(new OurFileUploadField());
	}
	
	private boolean verifyIfAbleToSendUpload(OurFileUploadField uploadFieldA, OurFileUploadField uploadFieldB, OurFileUploadField uploadFieldC) {
		return (uploadFieldA.getFilename().equals("") || uploadFieldB.getFilename().equals("")|| uploadFieldC.getFilename().equals(""));
	}
	
	private void createReservatorioInputFilesSpots() {
		reservatorioFileUploadField.setTitle("Reservoir File");
		
		reservatorioFileUploadField.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validReservatorioFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_RESERVATORIO, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getReservatorioFileUploadField(),getVolumesFileUploadField(), getScenarioFileUploadField())) {
					requestFileUpload();
				}
				
			}
		});
		
		reservatorioFileFieldSet.add(reservatorioFileUploadField);
	}
	
	private void createVolumesInputFilesSpots() {
		volumesFileUploadField.setTitle("Volumes File");
		
		volumesFileUploadField.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validVolumesFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_VOLUMES, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getReservatorioFileUploadField(),getVolumesFileUploadField(), getScenarioFileUploadField())) {
					requestFileUpload();
				}
			}
		});
		
		volumesFileFieldSet.add(volumesFileUploadField);
	}
	
	private void createScenarioInputFilesSpots() {
		scenarioFileUploadField.setTitle("Scenario File");
		
		scenarioFileUploadField.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validScenariosFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_SCENARIO, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getReservatorioFileUploadField(),getVolumesFileUploadField(), getScenarioFileUploadField())) {
					requestFileUpload();
				}
			}
		});
		
		scenarioFileFieldSet.add(scenarioFileUploadField);
	}
	
	private void createClimatePrevisionNumberFields() {
		setClimatePrevision();
		createClimatePrevisionNumberField(getDryNumberField(), "Dry");
		createClimatePrevisionNumberField(getNormalNumberField(), "Normal");
		createClimatePrevisionNumberField(getRainyNumberField(), "Rainy");
	}
	
	private void setClimatePrevision() {
		setDryNumberField(new NumberField());
		setNormalNumberField(new NumberField());
		setRainyNumberField(new NumberField());
	}
	
	private void createClimatePrevisionNumberField(NumberField numberField, String type) {
		numberField.setFieldLabel(type);
		numberField.setValue(0);
		numberField.setAllowBlank(false);
		numberField.setToolTip("The sum of this three field must add up to 1. If value is 0 will get information from .pes file");
		
		climatePrevisionFieldSet.add(numberField);
	}
	
	//Interface Execution
	
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
		reservatorioFileUploadField.setName(uploadId.toString());
		volumesFileUploadField.setName(uploadId.toString());
		scenarioFileUploadField.setName(uploadId.toString());

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
		return (reservatorioFileUploadField.getFilename().endsWith(".zip"));
	}
	
	private boolean validVolumesFileName() {
		return (volumesFileUploadField.getFilename().endsWith(".txt"));
	}
	
	private boolean validScenariosFileName() {
		return (scenarioFileUploadField.getFilename().endsWith(".cen"));
	}

	private boolean validateDryNormalRainyInput(double dry, double normal, double rainy) {
		if(dry == 0.0 && normal == 0.0 && rainy == 0.0) {
			return true;
		} else if((dry + normal + rainy) != 1.0) {
			MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_VALUES, null);
			return false;
		}
		return true;
	}
	
	private boolean validateInputs() {
		
		if(reservatorioFileUploadField.getFilename() == null || 
				volumesFileUploadField.getFilename() == null ||
				scenarioFileUploadField.getFilename() == null) {
			MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.MISSING_FILE, null);
			return false;
		} else if(getInitialPeriod().getValue() == null || getFinalPeriod().getValue() == null) {
			MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.DATE_ERROR, null);
			return false;
		}else if(!validReservatorioFileName()) {
			MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_RESERVATORIO, null);
			return false;
		}else if(!validVolumesFileName()) {
			MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_VOLUMES, null);
			return false;
		}else if(!validScenariosFileName()) {
			MessageBox.alert("Invalid Format", ReservatoriosJobSubmissionMessages.INVALID_UPLOAD_FILE_MSG_SCENARIO, null);
			return false;
		}else { 
			return validateDryNormalRainyInput(Double.parseDouble(getDryNumberField().getValue().toString()), Double.parseDouble(getNormalNumberField().getValue().toString()), Double.parseDouble(getRainyNumberField().getValue().toString()));
		}
		
	}
	
	private ReservatoriosJobSubmissionTO createReservatoriosJobSubmissionTO(Long uploadSessionId, 
			String userLogin,Double dry, Double normal, Double rainy, Integer startMonth,
			Integer endMonth, boolean complete,boolean readPesFromFile) {
		
		ReservatoriosJobSubmissionTO reservatoriosJobSubmissionTO = new ReservatoriosJobSubmissionTO();
		
		reservatoriosJobSubmissionTO.setExecutorName(CommonServiceConstants.RESERVATORIOS_JOB_SUBMISSION_EXECUTOR);
		reservatoriosJobSubmissionTO.setUploadId(uploadSessionId);
		reservatoriosJobSubmissionTO.setUserLogin(userLogin);
		reservatoriosJobSubmissionTO.setInputFile(createInputFileTO(uploadSessionId,dry, normal,rainy, startMonth, endMonth,complete,readPesFromFile));
		reservatoriosJobSubmissionTO.setJobName("Reservoir Job");
		reservatoriosJobSubmissionTO.setEmailNotification(false);
		
		return reservatoriosJobSubmissionTO;
	}

	private ReservatoriosInputFileTO createInputFileTO(Long uploadID,Double dry, Double normal, Double rainy, Integer startMonth, Integer endMonth,boolean complete, boolean readPesFromFile) {
		
		ReservatoriosInputFileTO inputFileTO = new ReservatoriosInputFileTO();
		
		inputFileTO.setReadPesFromFile(readPesFromFile);
		inputFileTO.setComplete(complete);
		inputFileTO.setDry(dry);
		inputFileTO.setNormal(normal);
		inputFileTO.setRainy(rainy);
		inputFileTO.setStartMonth(startMonth);
		inputFileTO.setEndMonth(endMonth);
		inputFileTO.setUploadID(uploadID);
		
		return inputFileTO;
	}
	
	private Integer getMonthIntegerValue(String month) {
		Map<Integer, String> map = ReservatoriosPropertiesUtil.getReservatoriosPropertiesSimulationPeriod();
		
		for (Integer monthValue : map.keySet()) {
			if(map.get(monthValue).equals(month)) {
				return monthValue;
			}
		}
		return null;
	}
	
	private boolean verifyIfReadFromPesFile(double dry, double normal, double rainy) {
		return (dry == 0 && normal == 0 && rainy == 0);
	}

	private void submitJob() {
		UserModel userModel = OurGridPortal.getUserModel();
		
		double dry = Double.parseDouble(getDryNumberField().getValue().toString()); 
		double normal = Double.parseDouble(getNormalNumberField().getValue().toString());
		double rainy = Double.parseDouble(getNormalNumberField().getValue().toString());
		
		boolean readPesFromFile = verifyIfReadFromPesFile(dry, normal, rainy);
		
		ReservatoriosJobSubmissionTO jobSubmissionTO = createReservatoriosJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin(),dry, 
			    normal,rainy,getMonthIntegerValue(initialPeriod.getValue().getMonth()), 
				getMonthIntegerValue(finalPeriod.getValue().getMonth()),verifyComplete(),readPesFromFile);
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<ReservatoriosJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(ReservatoriosJobSubmissionResponseTO result) {
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
		submitButton.enable();
		showJobStatusTree();
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

	private boolean verifyComplete() {
		return runCompleteButton.getValue();
	}

	private List<SimulationPeriodComboBox> createSimulationPeriodComboBoxStore(Map<Integer,String> map) {

		List<SimulationPeriodComboBox> store = new ArrayList<SimulationPeriodComboBox>();  
		map = ReservatoriosPropertiesUtil.getReservatoriosPropertiesSimulationPeriod();

		for (Integer month : map.keySet()) {
			store.add(new SimulationPeriodComboBox(map.get(month)));
		}
		return store;
	}

	
	public FieldSet getReservatorioFileFieldSet() {
		return reservatorioFileFieldSet;
	}

	public FieldSet getVolumesFileFieldSet() {
		return volumesFileFieldSet;
	}

	public FieldSet getScenarioFileFieldSet() {
		return scenarioFileFieldSet;
	}

	public void setReservatorioFileFieldSet(FieldSet reservatorioFileFieldSet) {
		this.reservatorioFileFieldSet = reservatorioFileFieldSet;
	}

	public void setVolumesFileFieldSet(FieldSet volumesFileFieldSet) {
		this.volumesFileFieldSet = volumesFileFieldSet;
	}

	public void setScenarioFileFieldSet(FieldSet scenarioFileFieldSet) {
		this.scenarioFileFieldSet = scenarioFileFieldSet;
	}
	
	public ComboBox<SimulationPeriodComboBox> getInitialPeriod() {
		return initialPeriod;
	}

	public ComboBox<SimulationPeriodComboBox> getFinalPeriod() {
		return finalPeriod;
	}
	
	public void setInitialPeriod(ComboBox<SimulationPeriodComboBox> initialPeriod) {
		this.initialPeriod = initialPeriod;
	}

	public void setFinalPeriod(ComboBox<SimulationPeriodComboBox> finalPeriod) {
		this.finalPeriod = finalPeriod;
	}

	public NumberField getDryNumberField() {
		return dryNumberField;
	}

	public void setDryNumberField(NumberField dryNumberField) {
		this.dryNumberField = dryNumberField;
	}

	public NumberField getNormalNumberField() {
		return normalNumberField;
	}

	public void setNormalNumberField(NumberField normalNumberField) {
		this.normalNumberField = normalNumberField;
	}

	public NumberField getRainyNumberField() {
		return rainyNumberField;
	}

	public void setRainyNumberField(NumberField rainyNumberField) {
		this.rainyNumberField = rainyNumberField;
	}

	public OurFileUploadField getReservatorioFileUploadField() {
		return reservatorioFileUploadField;
	}

	public void setReservatorioFileUploadField(
			OurFileUploadField reservatorioFileUploadField) {
		this.reservatorioFileUploadField = reservatorioFileUploadField;
	}

	public OurFileUploadField getVolumesFileUploadField() {
		return volumesFileUploadField;
	}

	public void setVolumesFileUploadField(OurFileUploadField volumesFileUploadField) {
		this.volumesFileUploadField = volumesFileUploadField;
	}

	public OurFileUploadField getScenarioFileUploadField() {
		return scenarioFileUploadField;
	}

	public void setScenarioFileUploadField(
			OurFileUploadField scenarioFileUploadField) {
		this.scenarioFileUploadField = scenarioFileUploadField;
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

