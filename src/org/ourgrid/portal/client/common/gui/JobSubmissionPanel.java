package org.ourgrid.portal.client.common.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.JobComboBoxModel;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.response.GetJobsUploadedNameResponseTO;
import org.ourgrid.portal.client.common.to.response.JobSubmissionResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UploadSessionIDResponseTO;
import org.ourgrid.portal.client.common.to.service.DeleteJobTO;
import org.ourgrid.portal.client.common.to.service.GetJobsUploadedNameTO;
import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UploadSessionIDTO;
import org.ourgrid.portal.client.common.util.JobSubmissionMessages;
import org.ourgrid.portal.client.common.util.OurFileUploadEventListener;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
  
public class JobSubmissionPanel extends LayoutContainer {  
  
  	private VerticalPanel mainPanel;
  	
  	private FormPanel jobSubmissionPanel;
  	
  	private OurFileUploadField fileUploadField;
  	
  	private HorizontalPanel jobSelectionPanel;
  	
  	private ComboBox<JobComboBoxModel> jobSelectionComboBox;
  	
  	private CheckBox emailNotification;
  
  	private TabPanel jobStatusTabPanel;
  	
  	private Integer jobTabCounter;
  	
  	private List<TabItem> tabs;

	private Button submitButton;
  	
  	public JobSubmissionPanel() {  
  		this.jobTabCounter = 0;
  		
	    init();
	    addListeners();
	    
	    add(mainPanel);
	    setAutoWidth(true);
	    setAutoHeight(true);
  	}  
  	
  	// LAYOUT
  	private void init() {
  		createMainPanel();
  		
  		createAndAddJobSubmissionPanel();
  		
  		createJobSelectionPanel();
  		createAndAddJobSelectionComboBox();
  		createAndAddSubmitButton();
  		finishJobSelectionPanel();
  		
  		createAndAddEmailNotificationCheckBox();
  		
  		createJobsTabPanel();
  	}

	private void createMainPanel() {
  		mainPanel = new VerticalPanel();  
	    mainPanel.setSpacing(0);
	    mainPanel.setWidth(650);	
  	}
  	
  	private void createAndAddJobSubmissionPanel() {
  		
  		jobSubmissionPanel = new FormPanel();
  		jobSubmissionPanel.setHeaderVisible(false);
	  
	    fileUploadField = new OurFileUploadField();
	    LayoutContainer container = new LayoutContainer();
	    container.setLayout(new RowLayout());
	    container.add(fileUploadField);
	    
	    jobSubmissionPanel.add(container);
	    mainPanel.add(jobSubmissionPanel);
  	}
  	
  	private void createJobSelectionPanel() {
  		jobSelectionPanel = new HorizontalPanel();  
	    jobSelectionPanel.setSpacing(5);  
  	}
  	
  	private void createAndAddJobSelectionComboBox() {
	    ListStore<JobComboBoxModel> store = new ListStore<JobComboBoxModel>();  
	    store.add(new ArrayList<JobComboBoxModel>());
	    
	    jobSelectionComboBox = new ComboBox<JobComboBoxModel>();
	    jobSelectionComboBox.setWidth(350);
	    
	    jobSelectionComboBox.setAllowBlank(true);
	    jobSelectionComboBox.setForceSelection(true);
	    jobSelectionComboBox.setTypeAhead(true);
	    jobSelectionComboBox.setSelectOnFocus(true);
	    
	    jobSelectionComboBox.setFieldLabel("Job Selection");  
	    jobSelectionComboBox.setDisplayField("jobName");  
	    jobSelectionComboBox.setName("jobName");  
	    jobSelectionComboBox.setValueField("jobName");
	    jobSelectionComboBox.setEmptyText("Select a job...");
	    jobSelectionComboBox.setStore(store);  
	    jobSelectionComboBox.setId("jobSelectionComboBoxIdJobs");
	    jobSelectionComboBox.setTriggerAction(TriggerAction.ALL); 
	    
	    jobSelectionPanel.add(jobSelectionComboBox);
  	}
  	
  	private void createAndAddSubmitButton() {
	    submitButton = new Button("Submit");
	    submitButton.setId("submitButtonIdJobs");
	    submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
	      public void componentSelected(ButtonEvent ce) { 
	    	submitJob();
	      }  
	    });
	    submitButton.setToolTip("Submit an zipped file with jdf and inputs files.");
	    jobSelectionPanel.add(submitButton);
  	}
  	
  	protected void submitJob() {
  		
		String jobName = jobSelectionComboBox.getValue().getJobName();
		
		if (jobName == null || jobName.length() == 0) {
			unmask();
			MessageBox.alert("Submission Error", JobSubmissionMessages.JDF_NOT_FOUND, null);
			return;
		}
		
		UserModel userModel = OurGridPortal.getUserModel();
		JobSubmissionTO jobSubmissionTO =
			createJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin(), jobName, emailNotification.getValue());
		
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<JobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
			}

			public void onSuccess(JobSubmissionResponseTO result) {
				jobSelectionComboBox.setValue(null);
				unmaskMainPanel();
				processJobSubmissionResponse(result);
			}
		});
	}

	protected void processJobSubmissionResponse(JobSubmissionResponseTO result) {
        
		Integer jobID = result.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		int nextJobViewId = getNextJobViewId();
		
		userModel.addJobId(nextJobViewId, jobID);
		userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
		
		TabItem tab = createAndAddJobStatusTab(nextJobViewId);
		tab.setClosable(false);
	}
	
	public TabItem createAndAddJobStatusTab(int jobViewId) {
		TabItem tab = addTab(jobViewId);
		jobStatusTabPanel.setSelection(jobStatusTabPanel.getItem(jobTabCounter - 1));
		return tab;
	}
	
	private TabItem addTab(int jobViewId) {
		final TabItem item = new TabItem();
		
		JobStatusPanel statusPanel = new JobStatusPanel(jobViewId, jobTabCounter, item);
		statusPanel.scheduleJobDescriptionRepeatedAction();
		
	    item.setLayout(new FitLayout());
	    item.setText("Job " + ++jobTabCounter);
		
	    item.add(statusPanel);
	    item.setClosable(true);
	    jobStatusTabPanel.setVisible(true);
	    jobStatusTabPanel.add(item);
	    
	    item.addListener(Events.BeforeClose, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				JobStatusPanel statusPanel = (JobStatusPanel) item.getWidget(0);
				handleTabClosed(statusPanel);
			}
  		});
	    
	    item.addListener(Events.Show, new Listener<BaseEvent>() {

			public void handleEvent(BaseEvent be) {
				JobStatusPanel statusPanel = (JobStatusPanel) item.getWidget(0);
				statusPanel.refresh();
			}
	    	
	    });
	    
	    return item;
	}
	
	public void createJobsTabs(List<AbstractRequest> requests) {
		
		if(!requests.isEmpty()){
			jobStatusTabPanel.setVisible(true);
		}
		
		UserModel userModel = OurGridPortal.getUserModel();
		
		this.tabs = new LinkedList<TabItem>();
		
		for (AbstractRequest request : requests) {
			
			int jobViewId = getNextJobViewId();
			int jobID = request.getJobID();
			
			userModel.addJobId(jobViewId, jobID);
			userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
			tabs.add(createAndAddJobStatusTab(jobViewId));
		}
	}

	private JobSubmissionTO createJobSubmissionTO(Long uploadSessionId, String userLogin,
			String jobName, Boolean emailNotification) {
		
		JobSubmissionTO jobSubmissionTO = new JobSubmissionTO();
		jobSubmissionTO.setExecutorName(CommonServiceConstants.JOB_SUBMISSION_EXECUTOR);
		jobSubmissionTO.setUploadId(uploadSessionId);
		jobSubmissionTO.setUserLogin(userLogin);
		jobSubmissionTO.setJobName(jobName);
		jobSubmissionTO.setEmailNotification(emailNotification);
		return jobSubmissionTO;
	}

	private void finishJobSelectionPanel() {
  		mainPanel.add(jobSelectionPanel);
  	}
  	
  	private void createAndAddEmailNotificationCheckBox() {
  		HorizontalPanel emailNotificationPanel = new HorizontalPanel();
  		
  		emailNotification = new CheckBox();
  		emailNotification.setBoxLabel("Email notification");
  		emailNotification.setToolTip("Select to receive e-mail when your job has finished.");
  		
  		emailNotificationPanel.add(emailNotification);
  		mainPanel.add(emailNotificationPanel);
  	}

  	private void createJobsTabPanel() {
  		
  		jobStatusTabPanel = new TabPanel();
  		jobStatusTabPanel.setSize(650, 360);  
  		jobStatusTabPanel.setMinTabWidth(115);  
  		jobStatusTabPanel.setResizeTabs(false);  
  		jobStatusTabPanel.setAnimScroll(true);  
  		jobStatusTabPanel.setTabScroll(true);
  		jobStatusTabPanel.setAutoWidth(true);
  		jobStatusTabPanel.setAutoHeight(true);
  		jobStatusTabPanel.setCloseContextMenu(true);
  		jobStatusTabPanel.setVisible(false);
	    
	    jobTabCounter = 0;
	    
	    mainPanel.add(jobStatusTabPanel);
	}
  	
  	// OPERATIONS
  	private void addListeners() {
  		jobSubmissionPanel.addListener(Events.Submit, new Listener<FormEvent>() {

			public void handleEvent(FormEvent be) {
				
				if (isQuotaExceed(be.getResultHtml())) {
					MessageBox.alert("Disk quota exceeded", "The specified file exceeds your quota.", null);
					unmaskMainPanel();
				} else {
					loadJDFComboBox();
					OurGridPortal.refreshFileExplorerRoot();
				}
			}
		});
  		
  		fileUploadField.addListener(new OurFileUploadEventListener() {
			public void onEvent(Event event) {
				maskMainPanel("Loading...");
				requestFileUpload();
			}
  		});
  		
  	}
  	
  	protected boolean isQuotaExceed(String resultHtml) {
		return resultHtml.contains(CommonServiceConstants.QUOTA_EXCEED_ERROR_CODE);
	}

	protected void handleTabClosed(JobStatusPanel statusPanel) {
  		
		statusPanel.stopStatusTimer();
			deleteJob(statusPanel.getJobId());
  		
		// this is made before the tab is closed, so if there's one tab, the job status is invisible
  		if (jobStatusTabPanel.getItems().size() == 1) {
  			jobStatusTabPanel.setVisible(false);
  		}
	}

	private void deleteJob(Integer jobId) {
		DeleteJobTO deleteJobTO = new DeleteJobTO();
  		deleteJobTO.setExecutorName(CommonServiceConstants.DELETE_JOB_EXECUTOR);
  		deleteJobTO.setJobId(jobId);
  		
		OurGridPortalServerUtil.getInstance().execute(deleteJobTO, new AsyncCallback<ResponseTO>() {

			public void onSuccess(ResponseTO result) {
				
			}
			
			public void onFailure(Throwable caught) {
				
			}
			
		});
	}

	protected void loadJDFComboBox() {
  		
		GetJobsUploadedNameTO serviceTO =
			createGetJobsUploadedNameServiceTO(OurGridPortal.getUserModel().getUploadSessionId());
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<GetJobsUploadedNameResponseTO>() {

			public void onSuccess(GetJobsUploadedNameResponseTO result) {
				unmask();
				List<String> jobsName = result.getJdfNames();
				
				if (jobsName.isEmpty()) {
					MessageBox.alert("Error", JobSubmissionMessages.JDF_NOT_FOUND, null);
					return;
				}
				updateJobSelectionComboBox(jobsName);
			}
			
			public void onFailure(Throwable caught) {
				unmaskMainPanel();
				MessageBox.alert("JDF Name Error", JobSubmissionMessages.LOAD_JDF_NAME_ERROR_MSG, null);
			}
			
		});
	}

	private GetJobsUploadedNameTO createGetJobsUploadedNameServiceTO(Long uploadId) {
		GetJobsUploadedNameTO getJobsUploadedNameTO = new GetJobsUploadedNameTO();
		getJobsUploadedNameTO.setExecutorName(CommonServiceConstants.GET_JOBS_UPLOADED_NAME_EXECUTOR);
		getJobsUploadedNameTO.setUploadSessionId(uploadId);
		
		return getJobsUploadedNameTO;
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
	    	MessageBox.alert("Invalid Format", JobSubmissionMessages.INVALID_UPLOAD_FILE_MSG, null);
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
  		
  		String formAction = CommonServiceConstants.UPLOAD_FORM_ACTION + 
  		CommonServiceConstants.USER_NAME_PARAMETER + "=" + userModel.getUserLogin() + "&" +
  		CommonServiceConstants.TO_DESCOMPACT + "=true&" +
  		CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=true&" +
  		CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId;
  		
  		jobSubmissionPanel.setAction(formAction);
  		jobSubmissionPanel.setMethod(Method.POST);
  		jobSubmissionPanel.setEncoding(Encoding.MULTIPART);
  		jobSubmissionPanel.submit();
  	}
  	
	private boolean validUploadFileName() {
		
		String[] validExtensions = new String[] {"zip", "tar.gz"};
		
		for (String validExtension : validExtensions) {
			if (fileUploadField.getFilename().endsWith(validExtension)) {
				return true;
			}
		}
		
		return false;
	}
  	
	private void updateJobSelectionComboBox(List<String> jobNames) {
		jobSelectionComboBox.getStore().removeAll();
		jobSelectionComboBox.getStore().add(createJobComboBoxModelStore(jobNames));
	}

	private List<JobComboBoxModel> createJobComboBoxModelStore(List<String> jobNames) {
	    List<JobComboBoxModel> store = new ArrayList<JobComboBoxModel>();  
		
		for (String name : jobNames) {
			store.add(new JobComboBoxModel(name));
		}
		return store;
	}
	
	public void configureTabs() {
		for (TabItem tab : tabs) {
			tab.setClosable(false);
		}
	}

	public void desactivateSubmission() {
		jobSubmissionPanel.setEnabled(false);
		emailNotification.setEnabled(false);
		jobSelectionComboBox.setEnabled(false);
		submitButton.setEnabled(false);
	}
	
	public void activateSubmission() {
		jobSubmissionPanel.setEnabled(true);
		emailNotification.setEnabled(true);
		jobSelectionComboBox.setEnabled(true);
		submitButton.setEnabled(true);
	}
}