package org.ourgrid.portal.client.plugin.fibonacci.gui;

import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsHelpWindow;

public class FibonacciSubmitJobsHelpWindow extends PluginSubmitJobsHelpWindow {

	public FibonacciSubmitJobsHelpWindow(String message) {
		super(message);
	}

	@Override
	protected String getHelpText() {
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application simply keeps calculating <b><a target=\"_blank\">Fibonacci</a></b> sequence.</span></p>"
				+ "<p><span style='line-height:10% ;font-size:10.0pt ; font-family:\"Arial\"'>The following steps should be taken to submit a Fibonacci Job:</span></p>"
				+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. You need to fill the field with the amount of jobs you want to create. </span></p>"
				+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. Click the <b>Submit</b> button.</span></p>"
				+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>After clicking the <b>Submit</b> button, you can monitor the status of the jobs you have submitted. When a task finishes, the job status tree shows the result node. So, you can download the task output by clicking on this link. You can also retrieve the output of your jobs using the File Explorer application. You can repeat the process and submit other jobs by clicking in the <b>New Job</b> button; each new job submitted will appear in a separate tab.</span></p>";
				return text;
	}

}
