package org.ourgrid.portal.client.plugin.autodock.gui;

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
import org.ourgrid.portal.client.plugin.autodock.to.model.AutodockInputFileTO;
import org.ourgrid.portal.client.plugin.autodock.to.response.AutodockJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.autodock.to.service.AutodockJobSubmissionTO;
import org.ourgrid.portal.client.plugin.autodock.util.AutodockJobSubmissionMessages;
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

public class AutodockJobSubmissionPanel extends PluginJobSubmissionPanel{

	//TODO CHANGE ICON PICTURE OF THE AUTODOCK PLUGIN
	private FieldSet			mainFieldSet;
	private FieldSet			macromoleculeRigidModelFieldSet;
	private FieldSet			macromoleculeFlexibleModelFieldSet;
	private FieldSet			gridParametersFieldSet;
	private FieldSet			ligantModelFieldSet;
	private FieldSet			dockingParametersFieldSet;
	private FieldSet 			inputFilesFieldSet;
	
	private OurFileUploadField 	macromoleculeRigidModelFileSpot;
	private OurFileUploadField 	macromoleculeFlexibleModelFileSpot;
	private OurFileUploadField 	gridParametersFileSpot;
	private OurFileUploadField 	ligantModelFileSpot;
	private OurFileUploadField 	dockingParametersFileSpot;
	
	private FormPanel			fileSubmissionPanel;
	
	private Button				submitButton;
	
	public AutodockJobSubmissionPanel(Integer jobTabCounter, int jobViewId,TabItem container) {
		super(jobTabCounter, jobViewId, container);
	}
	
	
	@Override
	protected void init() {

		createMainPanel();
		createMainFieldSet();
		
		createInputFilesFieldSet();
		createAndAddFileSubmissionPanel();
		
		createSubmitButton();
		
		createStatusFieldSet("Autodock Status");
		createStatusPanel(new AutodockJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
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
		
		container.add(getMacromoleculeRigidModelFieldSet());
		container.add(getMacromoleculeFlexibleModelFieldSet());
		container.add(getGridParametersFieldSet());
		container.add(getLigantModelFieldSet());
		container.add(getDockingParametersFieldSet());

		fileSubmissionPanel.add(container);
		inputFilesFieldSet.add(fileSubmissionPanel);
	}
	
	public FieldSet getMacromoleculeRigidModelFieldSet() {
		return macromoleculeRigidModelFieldSet;
	}


	public void setMacromoleculeRigidModelFieldSet(FieldSet macromoleculeRigidModelFieldSet) {
		this.macromoleculeRigidModelFieldSet = macromoleculeRigidModelFieldSet;
	}


	public FieldSet getMacromoleculeFlexibleModelFieldSet() {
		return macromoleculeFlexibleModelFieldSet;
	}


	public void setMacromoleculeFlexibleModelFieldSet(FieldSet macromoleculeFlexibleModelFieldSet) {
		this.macromoleculeFlexibleModelFieldSet = macromoleculeFlexibleModelFieldSet;
	}


	public FieldSet getGridParametersFieldSet() {
		return gridParametersFieldSet;
	}

	public void setGridParametersFieldSet(FieldSet gridParametersFieldSet) {
		this.gridParametersFieldSet = gridParametersFieldSet;
	}

	public FieldSet getLigantModelFieldSet() {
		return ligantModelFieldSet;
	}
	
	public void setLigantModelFieldSet(FieldSet ligantModelFieldSet) {
		this.ligantModelFieldSet = ligantModelFieldSet;
	}
	
	public FieldSet getDockingParametersFieldSet() {
		return dockingParametersFieldSet;
	}
	
	public void setDockingParametersFieldSet(FieldSet dockingParametersFieldSet) {
		this.dockingParametersFieldSet = dockingParametersFieldSet;
	}

	public OurFileUploadField getMacromoleculeRigidModelFileSpot() {
		return macromoleculeRigidModelFileSpot;
	}


	public void setMacromoleculeRigidModelFileSpot(OurFileUploadField macromoleculeRigidModelFileSpot) {
		this.macromoleculeRigidModelFileSpot = macromoleculeRigidModelFileSpot;
	}


	public OurFileUploadField getMacromoleculeFlexibleModelFileSpot() {
		return macromoleculeFlexibleModelFileSpot;
	}


	public void setMacromoleculeFlexibleModelFileSpot(OurFileUploadField macromoleculeFlexibleModelFileSpot) {
		this.macromoleculeFlexibleModelFileSpot = macromoleculeFlexibleModelFileSpot;
	}


	public OurFileUploadField getGridParametersFileSpot() {
		return gridParametersFileSpot;
	}
	
	public void setGridParametersFileSpot(OurFileUploadField gridParametersFileSpot) {
		this.gridParametersFileSpot = gridParametersFileSpot;
	}

	public OurFileUploadField getLigantModelFileSpot() {
		return ligantModelFileSpot;
	}
	
	public void setLigantModelFileSpot(OurFileUploadField ligantModelFileSpot) {
		this.ligantModelFileSpot = ligantModelFileSpot;
	}
	
	public OurFileUploadField getDockingParametersFileSpot() {
		return dockingParametersFileSpot;
	}
	
	public void setDockingParametersFileSpot(OurFileUploadField dockingParametersFileSpot) {
		this.dockingParametersFileSpot = dockingParametersFileSpot;
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
		setMacromoleculeRigidModelFieldSet(new FieldSet());
		setMacromoleculeFlexibleModelFieldSet(new FieldSet());
		setGridParametersFieldSet(new FieldSet());
		setLigantModelFieldSet(new FieldSet());
		setDockingParametersFieldSet(new FieldSet());
	}
	
	private void createInputFilesSpots() {
		setInputFilesFieldSet();
		
		createInputFilesFieldSet(getMacromoleculeRigidModelFieldSet(), "Macromolecule Rigid Model File");
		createInputFilesFieldSet(getMacromoleculeFlexibleModelFieldSet(), "Macromolecule Flexible Model File");
		createInputFilesFieldSet(getGridParametersFieldSet(), "Grid Parameters File");
		createInputFilesFieldSet(getLigantModelFieldSet(), "Ligant Model File");
		createInputFilesFieldSet(getDockingParametersFieldSet(), "Docking Parameters File");
		
		setInputFilesSpots();
		createMacromoleculeRigidModelInputFilesSpots();
		createMacromoleculeFlexibleModelInputFilesSpots();
		createGridParametersInputFilesSpots();
		createLigantModelInputFilesSpots();
		createDockingParametersInputFilesSpots();
	}
	
	private void createMacromoleculeRigidModelInputFilesSpots() {
		macromoleculeRigidModelFileSpot.setTitle("Macromolecule Rigid Model File");
		
		macromoleculeRigidModelFileSpot.addListener(new OurFileUploadEventListener() {
			
			@Override
			public void onEvent(Event event) {
				if(!validMacromoleculeRigidModelFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", AutodockJobSubmissionMessages.INVALID_FILE_EXTENSION_PDBQT_MSG, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMacromoleculeRigidModelFileSpot(),getMacromoleculeFlexibleModelFileSpot(), getGridParametersFileSpot(), getLigantModelFileSpot(), getDockingParametersFileSpot())) {
					requestFileUpload();
				}
				
			}
		});
		
		macromoleculeRigidModelFieldSet.add(macromoleculeRigidModelFileSpot);
	}
	
	private void createMacromoleculeFlexibleModelInputFilesSpots() {
		macromoleculeFlexibleModelFileSpot.setTitle("Macromolecule Flexible Model File");

		macromoleculeFlexibleModelFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validMacromoleculeFlexibleModelFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", AutodockJobSubmissionMessages.INVALID_FILE_EXTENSION_PDBQT_MSG, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMacromoleculeRigidModelFileSpot(),getMacromoleculeFlexibleModelFileSpot(), getGridParametersFileSpot(), getLigantModelFileSpot(), getDockingParametersFileSpot())) {
					requestFileUpload();
				}

			}
		});

		macromoleculeFlexibleModelFieldSet.add(macromoleculeFlexibleModelFileSpot);

	}
	
	private void createGridParametersInputFilesSpots() {
		gridParametersFileSpot.setTitle("Grid Parameters File");

		gridParametersFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validGridParametersFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", AutodockJobSubmissionMessages.INVALID_FILE_EXTENSION_GPF_MSG, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMacromoleculeRigidModelFileSpot(),getMacromoleculeFlexibleModelFileSpot(), getGridParametersFileSpot(), getLigantModelFileSpot(), getDockingParametersFileSpot())) {
					requestFileUpload();
				}

			}
		});

		gridParametersFieldSet.add(gridParametersFileSpot);

	}
	
	private void createLigantModelInputFilesSpots() {
		ligantModelFileSpot.setTitle("Ligant Model File");

		ligantModelFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validLigantModelFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", AutodockJobSubmissionMessages.INVALID_FILE_EXTENSION_PDBQT_MSG, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMacromoleculeRigidModelFileSpot(),getMacromoleculeFlexibleModelFileSpot(), getGridParametersFileSpot(), getLigantModelFileSpot(), getDockingParametersFileSpot())) {
					requestFileUpload();
				}

			}
		});

		ligantModelFieldSet.add(ligantModelFileSpot);

	}
	
	private void createDockingParametersInputFilesSpots() {
		dockingParametersFileSpot.setTitle("Docking Parameters File");

		dockingParametersFileSpot.addListener(new OurFileUploadEventListener() {

			@Override
			public void onEvent(Event event) {
				if(!validDockingParametersFileName()) {
					unmaskMainPanel();
					MessageBox.alert("Invalid Format", AutodockJobSubmissionMessages.INVALID_FILE_EXTENSION_DPF_MSG, null);
					return;
				}
				if(!verifyIfAbleToSendUpload(getMacromoleculeRigidModelFileSpot(),getMacromoleculeFlexibleModelFileSpot(), getGridParametersFileSpot(), getLigantModelFileSpot(), getDockingParametersFileSpot())) {
					requestFileUpload();
				}

			}
		});

		dockingParametersFieldSet.add(dockingParametersFileSpot);

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

		if(macromoleculeRigidModelFileSpot.getFilename() == null || 
				macromoleculeFlexibleModelFileSpot.getFilename() == null ||
				gridParametersFileSpot.getFilename() == null ||
				ligantModelFileSpot.getFilename() == null ||
				dockingParametersFileSpot.getFilename() == null) {
			MessageBox.alert("Invalid Format", AutodockJobSubmissionMessages.MISSING_FILE, null);
			return false;
		} 
		return true;
	}
	
	private void submitJob() {
		UserModel userModel = OurGridPortal.getUserModel();
		
		AutodockJobSubmissionTO jobSubmissionTO = createAutodockJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<AutodockJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(AutodockJobSubmissionResponseTO result) {
				unmaskMainPanel();
				doLayout();
				processJobSubmissionResponse(result);
				processStatus();
			}
		});
	}
	
	private AutodockJobSubmissionTO createAutodockJobSubmissionTO(Long uploadSessionId, String userLogin) {
		
		AutodockJobSubmissionTO autodockJobSubmissionTO = new AutodockJobSubmissionTO();
		
		autodockJobSubmissionTO.setExecutorName(CommonServiceConstants.AUTODOCK_JOB_SUBMISSION_EXECUTOR);
		autodockJobSubmissionTO.setUploadId(uploadSessionId);
		autodockJobSubmissionTO.setUserLogin(userLogin);
		
		autodockJobSubmissionTO.setMacromoleculeRigidModelInputFile(
									createMacromoleculeRigidModelInputFile(uploadSessionId));
		autodockJobSubmissionTO.setMacromoleculeFlexibleModelInputFile(
									createMacromoleculeFlexibleModelInputFile(uploadSessionId));
		autodockJobSubmissionTO.setGridParametersInputFile(
									createGridParametersInputFile(uploadSessionId));
		autodockJobSubmissionTO.setLigantModelInputFile(
									createLigantModelInputFile(uploadSessionId));
		autodockJobSubmissionTO.setDockingParametersInputFile(
									createDockingParametersInputFile(uploadSessionId));
		
		autodockJobSubmissionTO.setJobName("Autodock Job");
		autodockJobSubmissionTO.setEmailNotification(false);
		
		return autodockJobSubmissionTO;
	}

	private AutodockInputFileTO createMacromoleculeRigidModelInputFile(Long uploadId) {		
		return createInputFile(uploadId, macromoleculeRigidModelFileSpot.getFilename());
	}
	
	private AutodockInputFileTO createMacromoleculeFlexibleModelInputFile(Long uploadId) {
		return createInputFile(uploadId, macromoleculeFlexibleModelFileSpot.getFilename());
	}
	
	private AutodockInputFileTO createGridParametersInputFile(Long uploadId) {
		return createInputFile(uploadId, gridParametersFileSpot.getFilename());
	}
	
	private AutodockInputFileTO createLigantModelInputFile(Long uploadId) {
		return createInputFile(uploadId, ligantModelFileSpot.getFilename());
	}

	private AutodockInputFileTO createDockingParametersInputFile(Long uploadId) {
		return createInputFile(uploadId, dockingParametersFileSpot.getFilename());
	}
	
	private AutodockInputFileTO createInputFile(Long uploadId, String fileName) {
		AutodockInputFileTO inputFile = new AutodockInputFileTO();
		
		inputFile.setUploadId(uploadId);
		inputFile.setName(fileName);
		
		return inputFile;
	}


	private boolean verifyIfAbleToSendUpload(OurFileUploadField uploadFieldA, OurFileUploadField uploadFieldB, 
			OurFileUploadField uploadFieldC, OurFileUploadField uploadFieldD, OurFileUploadField uploadFieldE) {
		return (uploadFieldA.getFilename().equals("") || uploadFieldB.getFilename().equals("") || uploadFieldC.getFilename().equals("")
				|| uploadFieldD.getFilename().equals("") || uploadFieldE.getFilename().equals(""));
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
		macromoleculeRigidModelFileSpot.setName(uploadId.toString());
		macromoleculeFlexibleModelFileSpot.setName(uploadId.toString());
		gridParametersFileSpot.setName(uploadId.toString());
		ligantModelFileSpot.setName(uploadId.toString());
		dockingParametersFileSpot.setName(uploadId.toString());
		
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
	
	private boolean validMacromoleculeRigidModelFileName() {
		return (macromoleculeRigidModelFileSpot.getFilename().endsWith(".pdbqt"));
	}
	
	private boolean validMacromoleculeFlexibleModelFileName() {
		return (macromoleculeFlexibleModelFileSpot.getFilename().endsWith(".pdbqt"));
	}
	
	private boolean validGridParametersFileName() {
		return (gridParametersFileSpot.getFilename().endsWith(".gpf"));
	}
	
	private boolean validLigantModelFileName() {
		return (ligantModelFileSpot.getFilename().endsWith(".pdbqt"));
	}
	
	private boolean validDockingParametersFileName() {
		return (dockingParametersFileSpot.getFilename().endsWith(".dpf"));
	}
	
	private void setInputFilesSpots() {
		setMacromoleculeRigidModelFileSpot(new OurFileUploadField());
		setMacromoleculeFlexibleModelFileSpot(new OurFileUploadField());
		setGridParametersFileSpot(new OurFileUploadField());
		setLigantModelFileSpot(new OurFileUploadField());
		setDockingParametersFileSpot(new OurFileUploadField());
	}


	@Override
	public void desactivateSubmission() {
	}
	
	@Override
	protected void activateSubmission() {
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
	
	private void unmaskMainPanel() {
		this.el().unmask();
	}


	@Override
	protected void processInputs(List<?> inputs) {
		this.processStatus();
	}

}