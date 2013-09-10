package org.ourgrid.portal.client.plugin.fibonacci.gui;

import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.plugin.fibonacci.to.response.FibonacciJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.fibonacci.to.service.FibonacciJobSubmissionTO;
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FibonacciJobSubmissionPanel extends PluginJobSubmissionPanel {

	private FieldSet parametersFieldSet;
	private FieldSet submitFieldSet;
	private Button submitButton;
	
	public FibonacciJobSubmissionPanel(Integer jobTabCounter, int jobViewId,
			TabItem container) {
		
		super(jobTabCounter, jobViewId, container);
	}

	@Override
	protected void init() {
		createMainPanel();
		
		createSubmitFieldSet();
		createParametersFieldSet();
		createParametersPanel();
		
		createStatusFieldSet("Fibonacci Status");
		createStatusPanel(new FibonacciJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
	}
	
	/**
	 * Method that creates the outter fieldset
	 */
	private void createSubmitFieldSet() {
		submitFieldSet = new FieldSet();
		submitFieldSet.setHeading("Parameters");  
		submitFieldSet.setCollapsible(true);
		submitFieldSet.setWidth(SUBMIT_FIELDSET_WIDTH);
		submitFieldSet.setBorders(true);
  	  
		ColumnLayout layout = new ColumnLayout();
  	    
		submitFieldSet.setLayout(layout);
		submitFieldSet.setVisible(true);
  	    mainPanel.add(submitFieldSet);
		
	}
	
	/**
	 * Method that creates the parameters panel
	 */
	public void createParametersPanel() {
		
		FormPanel numberOfFibonacciPanel 	  = new FormPanel();

		final NumberField numberOfFibonacci   = new NumberField();
		
		submitButton = new Button("Submit");
	    
	    
	    submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
	    	public void componentSelected(ButtonEvent ce) { 
	    		if(!checkForNullValues(numberOfFibonacci)) {
	    			submitButton.disable();
					submitFibonnaciJob(numberOfFibonacci.getValue().intValue());
	    		} else {
	    			MessageBox.alert("Warning", "Empty parameter.", null);
	    		}
	    	}
	    });
	    
	    configureParameter(numberOfFibonacci);
	    
	    configurePanel(numberOfFibonacciPanel, numberOfFibonacci);
			    
	    parametersFieldSet.add(numberOfFibonacciPanel);
	    submitFieldSet.add(submitButton);
	}
	
	private boolean checkForNullValues(NumberField field1) {
		if(field1.getValue() == null) {
			field1.setAllowBlank(false);
			return true;
		}
		return false;
		
		
	}
	
	/**
	 * Method that creates the inner fieldset
	 */
	private void createParametersFieldSet() {
		
		parametersFieldSet = new FieldSet();
		parametersFieldSet.setWidth(SUBMIT_FIELDSET_WIDTH);
		parametersFieldSet.setBorders(false);
  	  
		ColumnLayout layout = new ColumnLayout();
  	    
		parametersFieldSet.setLayout(layout);
		parametersFieldSet.setVisible(true);
  	    submitFieldSet.add(parametersFieldSet);
	}
	
	
	/**
	 * @param panel
	 * @param fieldStart
	 */
	private void configurePanel(FormPanel panel, NumberField  fieldStart) {
		
	    panel.add(fieldStart);
	    
	    panel.setFrame(true); 
	    panel.setBorders(false);
	    panel.setAutoHeight(true);
	    panel.setAutoWidth(true);
	}
	
	/**
	 * Method that configure parameters
	 * @param fieldStart a fieldstart to be configured
	 * @param fieldEnd a fieldend to be configured
	 * @param fieldStep a fieldstep to be configured
	 */
	private void configureParameter(NumberField fieldStart) {
		fieldStart.setWidth(50);
		fieldStart.setFieldLabel("Number of tasks");
	}
	
	/**
	 * Method that creates a rhosubmissionTO
	 * @param uploadSessionId
	 * @param userLogin
	 * @return a rhojobsubmissionTO 
	 */
	private FibonacciJobSubmissionTO createFibonacciJobSubmissionTO(Long uploadSessionId, String userLogin) {
		
		FibonacciJobSubmissionTO fibonacciJobSubmissionTO = new FibonacciJobSubmissionTO();
		fibonacciJobSubmissionTO.setExecutorName(CommonServiceConstants.FIBONACCI_JOB_SUBMISSION_EXECUTOR);
		fibonacciJobSubmissionTO.setUploadId(uploadSessionId);
		fibonacciJobSubmissionTO.setUserLogin(userLogin);
		fibonacciJobSubmissionTO.setJobName("Fibonacci Job");
		
		return fibonacciJobSubmissionTO;
	}
	
	
	/**
	 * @param numOfFibonnaci
	 */
	private void submitFibonnaciJob(int numOfFibonnaci) {

		UserModel userModel = OurGridPortal.getUserModel();

		FibonacciJobSubmissionTO fibonacciJobSubmissionTO = createFibonacciJobSubmissionTO(userModel.getUploadSessionId(),
				userModel.getUserLogin());

		fibonacciJobSubmissionTO.setNumberOfTasks(numOfFibonnaci);
		
		OurGridPortalServerUtil.getInstance().execute(fibonacciJobSubmissionTO, new AsyncCallback<FibonacciJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				result.printStackTrace();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(FibonacciJobSubmissionResponseTO result) {
				processJobSubmissionResponse(result);
				processStatus();
				OurGridPortal.refreshFileExplorer();
			}

		});
	}

	@Override
	protected void desactivateSubmission() {
		submitFieldSet.setEnabled(false);

	}

	@Override
	protected void activateSubmission() {
		submitFieldSet.setEnabled(true);
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
	protected void processInputs(List<?> inputs) {
		this.processStatus();
	}

}
