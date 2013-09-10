package org.ourgrid.portal.client.plugin.gui;

import org.ourgrid.portal.client.OurGridPortal;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;

public abstract class PluginSubmitJobsHelpWindow extends Window {

	private final ContentPanel panel = new ContentPanel();
	
	public PluginSubmitJobsHelpWindow(String message) {
		super();
		init(message);
		
		createSubmitJobsHelpPanel();
		createSubmitJobsHelpText();

		this.add(panel);

	}
	
	private void createSubmitJobsHelpText() {
	    panel.addText(getHelpText());
		
	}

	private void init(String message) {
		this.setSize(500, 600);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(false);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading(message);
		this.setClosable(true);
		this.setScrollMode(Scroll.AUTO);
		
	}
	
	private void createSubmitJobsHelpPanel() {
		panel.setBodyBorder(false);
		panel.setBorders(false);
		panel.setAutoHeight(true);
		panel.setAutoWidth(true);
		panel.setHeaderVisible(false);
	}
	
	public void closeWindow() {
		this.setVisible(false);
		OurGridPortal.removeWindow(this);
	}
	
	protected abstract String getHelpText();

}
