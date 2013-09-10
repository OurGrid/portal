package org.ourgrid.portal.client.plugin.rho.gui;

import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.plugin.gui.PluginJobSubmissionPanel;
import org.ourgrid.portal.client.plugin.rho.to.response.RhoJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.rho.to.service.RhoJobSubmissionTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RhoJobSubmissionPanel extends PluginJobSubmissionPanel {
	
	private FieldSet parametersFieldSet;
	private FieldSet submitFieldSet;
	private Button submitButton;
	

	public RhoJobSubmissionPanel(Integer jobTabCounter, int jobViewId,
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
		createParametersFieldSet();
		createParametersPanel();
		
		createStatusFieldSet("SLinCA Status");
		createStatusPanel(new RhoJobStatusPanel(getJobViewId(), getJobTabCount(), getTabItem()));
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
		
		FormPanel parameterOnePanel 		  = new FormPanel();
		FormPanel parameterTwoPanel 		  = new FormPanel();
		FormPanel parameterThreePanel 		  = new FormPanel();
		FormPanel parameterFourPanel 		  = new FormPanel();
		FormPanel parameterFivePanel 		  = new FormPanel();
		FormPanel parameterSixPanel 		  = new FormPanel();
		FormPanel parameterSevenPanel 		  = new FormPanel();

		final NumberField parameterOneStart   = new NumberField();
		final NumberField parameterTwoStart   = new NumberField();
		final NumberField parameterThreeStart = new NumberField();
		final NumberField parameterFourStart  = new NumberField();
		final NumberField parameterFiveStart  = new NumberField();
		final NumberField parameterSixStart   = new NumberField();
		final NumberField parameterSevenStart = new NumberField();
		
		final NumberField parameterOneEnd     = new NumberField();
		final NumberField parameterTwoEnd     = new NumberField();
		final NumberField parameterThreeEnd   = new NumberField();
		final NumberField parameterFourEnd    = new NumberField();
		final NumberField parameterFiveEnd    = new NumberField();
		final NumberField parameterSixEnd     = new NumberField();
		final NumberField parameterSevenEnd   = new NumberField();
		
		final NumberField parameterOneStep 	  = new NumberField();
		final NumberField parameterTwoStep 	  = new NumberField();
		final NumberField parameterThreeStep  = new NumberField();
		final NumberField parameterFourStep   = new NumberField();
		final NumberField parameterFiveStep   = new NumberField();
		final NumberField parameterSixStep    = new NumberField();
		final NumberField parameterSevenStep  = new NumberField();
		
		submitButton = new Button("Submit");
	    
	    
	    submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
	    	public void componentSelected(ButtonEvent ce) { 
	    		if(!checkForNullValues(parameterOneStart,parameterTwoStart,parameterThreeStart,parameterFourStart,parameterFiveStart,parameterSixStart,parameterSevenStart)) {
	    			submitButton.disable();
					submitRhoJob(parameterOneStart.getValue().intValue(),
							parameterTwoStart.getValue().intValue(),
							parameterThreeStart.getValue().intValue(),
							parameterFourStart.getValue().intValue(),
							parameterFiveStart.getValue().intValue(),
							parameterSixStart.getValue().intValue(),
							parameterSevenStart.getValue().intValue(),
							
							parameterOneEnd.getValue().intValue(),
							parameterTwoEnd.getValue().intValue(),
							parameterThreeEnd.getValue().intValue(),
							parameterFourEnd.getValue().intValue(),
							parameterFiveEnd.getValue().intValue(),
							parameterSixEnd.getValue().intValue(),
							parameterSevenEnd.getValue().intValue(),
							
							parameterOneStep.getValue().intValue(),
							parameterTwoStep.getValue().intValue(),
							parameterThreeStep.getValue().intValue(),
							parameterFourStep.getValue().intValue(),
							parameterFiveStep.getValue().intValue(),
							parameterSixStep.getValue().intValue(),
							parameterSevenStep.getValue().intValue());
	    		} else {
	    			MessageBox.alert("Warning", "Empty parameter.", null);
	    		}
	    	}
	    });
	    
	    configureParameter(parameterOneStart,parameterOneEnd, parameterOneStep);
	    configureParameter(parameterTwoStart,parameterTwoEnd, parameterTwoStep);
	    configureParameter(parameterThreeStart,parameterThreeEnd, parameterThreeStep);
	    configureParameter(parameterFourStart,parameterFourEnd, parameterFourStep);
	    configureParameter(parameterFiveStart,parameterFiveEnd, parameterFiveStep);
	    configureParameter(parameterSixStart,parameterSixEnd, parameterSixStep);
	    configureParameter(parameterSevenStart,parameterSevenEnd, parameterSevenStep);
	    
	    configurePanel(parameterOnePanel, "Current Iteration", parameterOneStart, parameterOneEnd, parameterOneStep);
	    configurePanel(parameterTwoPanel, "Cluster Geometry Label", parameterTwoStart, parameterTwoEnd, parameterTwoStep);
	    configurePanel(parameterThreePanel, "Cluster Surface Dimension", parameterThreeStart, parameterThreeEnd, parameterThreeStep);
	    configurePanel(parameterFourPanel, "Cluster Volume Dimension", parameterFourStart, parameterFourEnd, parameterFourStep);
	    configurePanel(parameterFivePanel, "Number of Monomers in Cluster", parameterFiveStart, parameterFiveEnd, parameterFiveStep);
	    configurePanel(parameterSixPanel, "Number of Clusters", parameterSixStart, parameterSixEnd, parameterSixStep);
	    configurePanel(parameterSevenPanel, "Number of MC Steps", parameterSevenStart, parameterSevenEnd, parameterSevenStep);
			    
	    parametersFieldSet.add(parameterOnePanel);
	    parametersFieldSet.add(parameterTwoPanel);
	    parametersFieldSet.add(parameterThreePanel);
	    parametersFieldSet.add(parameterFourPanel);
	    parametersFieldSet.add(parameterFivePanel);
	    parametersFieldSet.add(parameterSixPanel);
	    parametersFieldSet.add(parameterSevenPanel);
	    submitFieldSet.add(submitButton);
	}
	
	private boolean checkForNullValues(NumberField field1 ,NumberField field2, NumberField field3,
			NumberField field4, NumberField field5, NumberField field6, NumberField field7) {
		if(field1.getValue() == null) {
			field1.setAllowBlank(false);
			return true;
		}
		if(field2.getValue() == null) {
			field2.setAllowBlank(false);
			return true;
		}
		if(field3.getValue() == null) {
			field3.setAllowBlank(false);
			return true;
		}
		if(field4.getValue() == null) {
			field4.setAllowBlank(false);
			return true;
		}
		if(field5.getValue() == null) {
			field5.setAllowBlank(false);
			return true;
		}
		if(field6.getValue() == null) {
			field6.setAllowBlank(false);
			return true;
		}
		if(field7.getValue() == null) {
			field7.setAllowBlank(false);
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
	 * Method that configure the parameters' formpanels
	 * @param panel the panel that is to be configured
	 * @param heading the header of the panel
	 * @param fieldStart a fieldstart that goes in the panel
	 * @param fieldEnd a fieldend that goes in the panel
	 * @param fieldStep a fieldstep that goes in the panel
	 */
	private void configurePanel(FormPanel panel, String heading, NumberField  fieldStart,
			NumberField  fieldEnd, NumberField  fieldStep) {
		
		panel.setHeading(heading);
	    panel.add(fieldStart);
	    panel.add(fieldEnd);
	    panel.add(fieldStep);
	    
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
	private void configureParameter(NumberField fieldStart, final NumberField fieldEnd, NumberField fieldStep) {
		fieldStart.setWidth(50);
		fieldStart.setFieldLabel("First Value");
		fieldEnd.setWidth(50);
		fieldEnd.setFieldLabel("Last Value");
		fieldStep.setWidth(50);
		fieldStep.setFieldLabel("Step");
		fieldStep.setValue(1);
		
		fieldStart.addListener(Events.KeyUp, new Listener<BaseEvent>() {

			public void handleEvent(BaseEvent be) {
				NumberField source = (NumberField) be.getSource();
				Number value = source.getValue();
				fieldEnd.setValue(value);
			}
		});
	}
	
	/**
	 * Method that creates a rhosubmissionTO
	 * @param uploadSessionId
	 * @param userLogin
	 * @return a rhojobsubmissionTO 
	 */
	private RhoJobSubmissionTO createRhoJobSubmissionTO(Long uploadSessionId, String userLogin) {
		
		RhoJobSubmissionTO rhoJobSubmissionTO = new RhoJobSubmissionTO();
		rhoJobSubmissionTO.setExecutorName(CommonServiceConstants.RHO_JOB_SUBMISSION_EXECUTOR);
		rhoJobSubmissionTO.setUploadId(uploadSessionId);
		rhoJobSubmissionTO.setUserLogin(userLogin);
		rhoJobSubmissionTO.setJobName("SLinCA Job");
		
		return rhoJobSubmissionTO;
	}

	/**
	 * Method that configure the rhojobsumissionto and submit it to executor
	 * @param parameterOneStart
	 * @param parameterTwoStart
	 * @param parameterThreeStart
	 * @param parameterFourStart
	 * @param parameterFiveStart
	 * @param parameterSixStart
	 * @param parameterSevenStart
	 * @param parameterOneEnd
	 * @param parameterTwoEnd
	 * @param parameterThreeEnd
	 * @param parameterFourEnd
	 * @param parameterFiveEnd
	 * @param parameterSixEnd
	 * @param parameterSevenEnd
	 * @param parameterOneStep
	 * @param parameterTwoStep
	 * @param parameterThreeStep
	 * @param parameterFourStep
	 * @param parameterFiveStep
	 * @param parameterSixStep
	 * @param parameterSevenStep
	 */
	private void submitRhoJob(int parameterOneStart,int parameterTwoStart,int parameterThreeStart,
			int parameterFourStart,int parameterFiveStart,int parameterSixStart,
			int parameterSevenStart,int parameterOneEnd,int parameterTwoEnd,
			int parameterThreeEnd,int parameterFourEnd,int parameterFiveEnd,
			int parameterSixEnd,int parameterSevenEnd,int parameterOneStep,
			int parameterTwoStep,int parameterThreeStep,int parameterFourStep,
			int parameterFiveStep,int parameterSixStep,int parameterSevenStep) {

		UserModel userModel = OurGridPortal.getUserModel();

		RhoJobSubmissionTO rhoJobSubmissionTO = createRhoJobSubmissionTO(userModel.getUploadSessionId(),
				userModel.getUserLogin());

		rhoJobSubmissionTO.setParameterOneStart(parameterOneStart);
		rhoJobSubmissionTO.setParameterTwoStart(parameterTwoStart);
		rhoJobSubmissionTO.setParameterThreeStart(parameterThreeStart);
		rhoJobSubmissionTO.setParameterFourStart(parameterFourStart);
		rhoJobSubmissionTO.setParameterFiveStart(parameterFiveStart);
		rhoJobSubmissionTO.setParameterSixStart(parameterSixStart);
		rhoJobSubmissionTO.setParameterSevenStart(parameterSevenStart);

		rhoJobSubmissionTO.setParameterOneEnd(parameterOneEnd);
		rhoJobSubmissionTO.setParameterTwoEnd(parameterTwoEnd);
		rhoJobSubmissionTO.setParameterThreeEnd(parameterThreeEnd);
		rhoJobSubmissionTO.setParameterFourEnd(parameterFourEnd);
		rhoJobSubmissionTO.setParameterFiveEnd(parameterFiveEnd);
		rhoJobSubmissionTO.setParameterSixEnd(parameterSixEnd);
		rhoJobSubmissionTO.setParameterSevenEnd(parameterSevenEnd);

		rhoJobSubmissionTO.setParameterOneStep(parameterOneStep);
		rhoJobSubmissionTO.setParameterTwoStep(parameterTwoStep);
		rhoJobSubmissionTO.setParameterThreeStep(parameterThreeStep);
		rhoJobSubmissionTO.setParameterFourStep(parameterFourStep);
		rhoJobSubmissionTO.setParameterFiveStep(parameterFiveStep);
		rhoJobSubmissionTO.setParameterSixStep(parameterSixStep);
		rhoJobSubmissionTO.setParameterSevenStep(parameterSevenStep);
		
		OurGridPortalServerUtil.getInstance().execute(rhoJobSubmissionTO, new AsyncCallback<RhoJobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				result.printStackTrace();
				MessageBox.alert("Submission Error", result.getMessage(), null);
				submitButton.enable();
			}

			public void onSuccess(RhoJobSubmissionResponseTO result) {
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

}