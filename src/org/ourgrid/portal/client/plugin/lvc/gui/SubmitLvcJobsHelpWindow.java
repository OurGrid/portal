package org.ourgrid.portal.client.plugin.lvc.gui;

import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsHelpWindow;

public class SubmitLvcJobsHelpWindow extends PluginSubmitJobsHelpWindow{

	public SubmitLvcJobsHelpWindow(String message) {
		super(message);
	}

	@Override
	protected String getHelpText() {
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application provides a simple way to create jobs that use the <b><a target=\"_blank\">SLinCA</a></b> application. SLinCA application is computation and output data intensive.</span></p>"
			+ "<p><span style='line-height:10% ;font-size:10.0pt ; font-family:\"Arial\"'>The following steps should be taken to submit a SLinCA Job:</span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. You need to fill the follows fields that represents the first value, last value and step number of each physical parameters and sizes of simulated systems. They are need to  build the workunit parameter file which contains characteristics of unique kinetic Monte Carlo scenario. </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>The parameters are: </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Current iteration number </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Geometry </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Cluster surface dimension </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Cluster volume dimension </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Initial number of interacting agents </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Initial number of clusters  </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'> - Total number of MCSs </span></p>"
			+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. Click the <b>Submit</b> button.</span></p>"
			+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>After clicking the <b>Submit</b> button, you can monitor the status of the jobs you have submitted. When a task finishes, the job status tree shows the result node. So, you can download the task output by clicking on this link. You can also retrieve the output of your jobs using the File Explorer application. You can repeat the process and submit other jobs by clicking in the <b>New Job</b> button; each new job submitted will appear in a separate tab.</span></p>";
			return text;
	}
}