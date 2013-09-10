package org.ourgrid.portal.client.plugin.gui;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public abstract class PluginSubmitJobsWindow extends Window {
	
	private FormPanel formPanel = new FormPanel();
	private PluginCreateJobPanel jobPanel;
	private ToolBar newJobToolBar;
	protected LinkedList<TabItem> tabs;
	protected Button newJobButton;
	protected PluginSubmitJobsHelpWindow submitJobsHelpWindow;
	
	public PluginSubmitJobsWindow(List<AbstractRequest> list, String message,PluginCreateJobPanel jobPanel) {
		super();
		init(message);
		
		createSubmitJobsForm();
		createAndAddNewJobButton();
		createListener();
		
		this.jobPanel = jobPanel;
		jobPanel.setAutoHeight(true);
		jobPanel.setAutoWidth(true);
		formPanel.add(jobPanel);
		
		initializeVariables();
		
		this.setTopComponent(newJobToolBar);
		this.add(formPanel);

		createJobsTabs(list);
	}
	
	protected abstract void initializeVariables();
	
	private void createListener() {
		this.addListener(Events.Close, new Listener<WindowEvent>() {

			@Override
			public void handleEvent(WindowEvent be) {
				OurGridPortal.getDesktop().removeWindow(OurGridPortal.getSubmitJobsWindow());
			}

		});
	}
	
	private void createAndAddNewJobButton() {
		this.newJobToolBar = new ToolBar();
		newJobButton = new Button("New Job");
		newJobButton.setBorders(true);
		newJobButton.setVisible(true);
		newJobButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			public void componentSelected(ButtonEvent ce) {
				TabItem newJob = newJob();
				newJob.setClosable(true);
			}
			
		});
		
		this.newJobToolBar.add(newJobButton);
		this.add(this.newJobToolBar);
	}
	
	protected TabItem newJob() {
		return jobPanel.addTabJob();
	}

	public void createJobsTabs(List<AbstractRequest> jobsList){
		
		this.tabs = new LinkedList<TabItem>();
		
		if (!jobsList.isEmpty()) {
			for (AbstractRequest abstractRequest : jobsList) {
				tabs.add(jobPanel.addTab(abstractRequest));
			}
		}
	}
	
	protected void init(String message) {
		
		this.setSize(800, 650);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(false);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading(message);
		this.setClosable(true);
		
		this.getHeader().addTool(  
	            new ToolButton("x-tool-help", new SelectionListener<IconButtonEvent>() {  
	      
	              @Override  
	              public void componentSelected(IconButtonEvent ce) {  
	            	  showSubmitJobsHelpWindow();
	              }  
	      
	    })); 
	}
	
	public void showSubmitJobsHelpWindow(){
		Desktop desktop = OurGridPortal.getDesktop();
		
		desktop.addWindow(submitJobsHelpWindow);
		desktop.minimizeWindow(submitJobsHelpWindow);
		submitJobsHelpWindow.show();
	}
	
	private void createSubmitJobsForm() {
		formPanel.setBodyBorder(false);
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
		if (tabs.isEmpty()) {
			tabs.add(newJob());
		} else {
			for (TabItem tab : tabs) {
				tab.setClosable(false);
			}
		}
	}

	public abstract void desactivateSubmission();

	public abstract void activateSubmission();
}
