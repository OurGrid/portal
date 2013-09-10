package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.OurGridPortal;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;

public class SubmitJobsHelpWindow extends Window {
	
	private final ContentPanel panel = new ContentPanel();
	
	public SubmitJobsHelpWindow() {
		super();
		init();
		
		createSubmitBlenderJobsHelpPanel();
		createSubmitBlenderJobsHelpText();

		this.add(panel);

	}
	
	private void createSubmitBlenderJobsHelpText() {
	    panel.addText(getHelpText());
		
	}

	private void init() {
		this.setSize(500, 400);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(false);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading("Submit Jobs Help");
		this.setClosable(true);
		this.setScrollMode(Scroll.AUTO);
		
	}
	
	private void createSubmitBlenderJobsHelpPanel() {
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
		
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application provides a simple way of running a job on the <b><a href=\"http://www.ourgrid.org/\" target=\"_blank\">OurGrid</a></b> Community grid. All you have to do is:</span></p>" 
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. Write your Job Description File (.jdf) (see how to write a jdf <b><a href=\"http://www.ourgrid.org/index.php?option=com_content&view=article&id=67&Itemid=2&lang=pt\" target=\"_blank\">here</a></b>);</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. Prepare a tarball (.tar.gz) or a Zip file (.zip) containing your .jdf, with the executable files of the program and all input files needed;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3. Upload the tarball or Zip file;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>4. Select a job at the combo box;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>5. Mark the email notification checkbox if you would like to receive an email message notifying the completion of your job;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>6. Click the <b>Submit</b> button.</span></p>"
					+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>After clicking the <b>Submit</b> button, you can monitor the status of the jobs you have submitted. When a task finishes, the job status tree shows the result node. So, you can download the task output by clicking on this link. You can also retrieve the output of your jobs using the File Explorer application. You can repeat the process and submit other jobs; each new job submitted will appear in a separate tab.</span></p>"; 

		return text;
	}

}