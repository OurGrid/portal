package org.ourgrid.portal.client.common.gui;

import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

public class SubmitJobsWindow extends Window {
	
	private final FormPanel formPanel = new FormPanel();
	private final JobSubmissionPanel tabJobs;
	private final SubmitJobsHelpWindow submitJobsHelpWindow = new SubmitJobsHelpWindow();
	
	public SubmitJobsWindow(List<AbstractRequest> jobsList) {
		super();
		init();
		
		createSubmitJobsForm();
		
		createListener();
		
		tabJobs = new JobSubmissionPanel();
		formPanel.add(tabJobs);
		
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		this.add(formPanel);
		
		createJobsTabs(jobsList);
	}
	
	private void createListener() {
		this.addListener(Events.Close, new Listener<WindowEvent>() {

			//TODO Nao esta funcionando :~
			@Override
			public void handleEvent(WindowEvent be) {
				OurGridPortal.getDesktop().removeWindow(OurGridPortal.getSubmitJobsWindow());
			}

		});
	}

	public void createJobsTabs(List<AbstractRequest> jobsList){
		tabJobs.createJobsTabs(jobsList);
	}
	
	private void init() {
		this.setSize(730, 525);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading("Submit OurGrid Jobs");
		this.setClosable(true);
		
		this.getHeader().addTool(  
	            new ToolButton("x-tool-help", new SelectionListener<IconButtonEvent>() {  
	      
	              @Override  
	              public void componentSelected(IconButtonEvent ce) {  
	            	  showSubmitJobsHelpWindow();
	              }  
	      
	    })); 
	}
	
	private void createSubmitJobsForm() {
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.setHeaderVisible(false);
	}
	
	public void closeWindow() {
		this.setVisible(false);
		OurGridPortal.removeWindow(this);
		
		this.submitJobsHelpWindow.closeWindow();
	}

	public void configureTabs() {
		tabJobs.configureTabs();
	}

	public void createAndAddJobStatusTab(int jobViewId) {
		tabJobs.createAndAddJobStatusTab(jobViewId);
	}

	public void desactivateSubmission() {
		tabJobs.desactivateSubmission();
	}
	
	public void activateSubmission(){
		tabJobs.activateSubmission();
	}

	public Window getSubmitJobsHelpWindow() {
		return submitJobsHelpWindow;
	}
	
	public void showSubmitJobsHelpWindow(){
		Desktop desktop = OurGridPortal.getDesktop();
		
		desktop.addWindow(submitJobsHelpWindow);
		desktop.minimizeWindow(submitJobsHelpWindow);
		submitJobsHelpWindow.show();
	}

}