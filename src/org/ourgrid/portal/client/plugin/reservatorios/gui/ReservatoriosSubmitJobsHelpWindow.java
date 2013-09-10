package org.ourgrid.portal.client.plugin.reservatorios.gui;

import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsHelpWindow;

public class ReservatoriosSubmitJobsHelpWindow extends PluginSubmitJobsHelpWindow {

	public ReservatoriosSubmitJobsHelpWindow(String message) {
		super(message);
	}

	@Override
	protected String getHelpText() {
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application provides a simple way to create jobs that use the <b><a target=\"_blank\">Reservoir</a></b> application. Reservoir is an application that allow the user to estimate the volume of reservoirs.</span></p>"
		+ "<p><span style='line-height:10% ;font-size:10.0pt ; font-family:\"Arial\"'>The following steps should be taken to submit a Reservoir Job:</span></p>"
		+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. Select the input reservoir file (.zip);</span></p>"
		+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. Select the input volumes file (.txt);</span></p>"
		+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3. Select the input scenarios file (.cen);</span></p>"
		+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>4. Enter with the values for dry, normal and rainy fields. The sum of these three values must add up to 1;</span></p>"
		+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>5. Select the starting month;</span></p>"
		+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>6. Select the ending month;</span></p>"
    	+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>7. Click the <b>Submit</b> button.</span></p>"
		+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>After clicking the <b>Submit</b> button, you can monitor the status of the jobs you have submitted. When a task finishes, the job status tree shows the result node. So, you can download the task output by clicking on this link. You can also retrieve the output of your jobs using the File Explorer application. You can repeat the process and submit other jobs by clicking in the <b>New Job</b> button; each new job submitted will appear in a separate tab.</span></p>";
		return text;
	}

}
