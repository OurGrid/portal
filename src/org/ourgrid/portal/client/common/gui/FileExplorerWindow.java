package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.to.model.FileTO;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class FileExplorerWindow extends Window {

	private final FileExplorerPanel fileExplorer;
	private static TextField<String> location;
	private final FileExplorerHelpWindow fileExplorerHelpWindow = new FileExplorerHelpWindow();
	
	public FileExplorerWindow(FileTO fileTO, Double initialQuota) {
		super();
		init();
		
		createListener();
		
		fileExplorer = new FileExplorerPanel(fileTO, initialQuota);
		this.add(fileExplorer);
		
	}
	
	public static TextField<String> getLocation(){
		return location;
	}
	  
	private void createListener() {
		this.addListener(Events.Close, new Listener<WindowEvent>() {

			@Override
			public void handleEvent(WindowEvent be) {
				closeWindow();
			}

		});
	}

	private void init() {
		this.setSize(730, 515);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading("File Explorer");
		this.setClosable(true);
		
		this.getHeader().addTool(  
	            new ToolButton("x-tool-help", new SelectionListener<IconButtonEvent>() {  
	      
	              @Override  
	              public void componentSelected(IconButtonEvent ce) {  
	            	  showFileExplorerHelpWindow();
	              }  
	      
	    })); 
	}
	
	public void showFileExplorerHelpWindow(){
		Desktop desktop = OurGridPortal.getDesktop();
		
		desktop.addWindow(fileExplorerHelpWindow);
		desktop.minimizeWindow(fileExplorerHelpWindow);
		fileExplorerHelpWindow.show();
	}
	
	public void closeWindow() {
		this.setVisible(false);
		OurGridPortal.removeWindow(this);
		
		this.fileExplorer.closeWindow();
		this.fileExplorerHelpWindow.closeWindow();
	}

	public void refresh() {
		this.doLayout();
	}

	public void activateQuotaOperations() {
		fileExplorer.activateQuotaOperations();
		refresh();
	}

	public void desactivateQuotaOperations() {
		fileExplorer.desactivateQuotaOperations();
		refresh();
	}

	public void refreshFileExplorer() {
		fileExplorer.refresh();
		refresh();
	}

	public void expandRoot() {
		fileExplorer.expandRoot();
	}
	
	public void explorerNode(FileTO fileTO){
		fileExplorer.explorerNode(fileTO);
	}

	public void updateFileInfoBar() {
		fileExplorer.updateFileInfoBar();
		
	}
	
	public void refreshRoot(){
		fileExplorer.refreshRoot();
	}

}