package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.OurGridPortal;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;

public class FileExplorerHelpWindow extends Window{

	private final ContentPanel panel = new ContentPanel();
	
	public FileExplorerHelpWindow() {
		super();
		init();
		
		createFileExplorerHelpPanel();
		createFileExplorerHelpText();

		this.add(panel);

	}
	
	private void createFileExplorerHelpText() {
	    panel.addText(getHelpText());
		
	}

	private void init() {
		this.setSize(500, 260);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(false);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading("File Explorer Help");
		this.setClosable(true);
		
	}
	
	private void createFileExplorerHelpPanel() {
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
	
	private String getHelpText(){
		
		
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application provides a windows-explorer-like interface, where you can <b>upload files</b>, <b>create folders</b>, <b>cut</b>, <b>copy</b>, <b>paste</b> and <b>delete</b> files and folders.</span></p>"
					+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>Right-clicking the mouse on a selected file allows you to <b>download</b>, <b>rename</b>, <b>copy</b>, <b>paste</b> or <b>cut</b> the selected file. OurGrid jobs are specified in .jdf files. Right-clicking on this files provides also a short-cut for the submission of new instances of the selected job. If no files are selected, you can <b>create a new folder</b> by right-clicking the mouse.</span></p>"
					+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>You should pay attention to your quota. You can follow it on the percentage bar below. If you exceed your quota, then you will not be able to submit new jobs or create new files, until you remove enough files to make the total amount of data stores smaller than your quota.</span></p>";

		return text;
	}
}
