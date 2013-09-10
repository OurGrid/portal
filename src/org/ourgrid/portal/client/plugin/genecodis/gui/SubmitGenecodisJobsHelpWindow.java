package org.ourgrid.portal.client.plugin.genecodis.gui;

import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsHelpWindow;

public class SubmitGenecodisJobsHelpWindow extends PluginSubmitJobsHelpWindow {
	
	public SubmitGenecodisJobsHelpWindow(String message) {
		super(message);

	}
	
	@Override
	public String getHelpText(){
		
		
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application provides a simple way to create jobs that use the <b><a href=\"http://genecodis.dacya.ucm.es/\" target=\"_blank\">Genecodis</a></b> application. Genecodis is a tool that integrates different sources of biological information to search for biological features (annotations) that frequently co-occur in a set of genes and rank them by statistical significance.</span></p>"
					+ "<p><span style='line-height:10% ;font-size:10.0pt ; font-family:\"Arial\"'>The following steps should be taken to submit a Genecodis Job:</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. Select the input file (.engene);</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. Click the checkbox, if you want to generate all possibilities of execution; it will combine all parameters, creating one task for each combination;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3. Set up the following fields:</span></p>"
					+ "<p style='text-indent:50pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3.1. Support: Minimum number of genes required that have to be implicated in a rule to take it into account;</span></p>"
					+ "<p style='text-indent:50pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3.2. Analysis: Concurrence analysis or Singular analysis;</span></p>"
					+ "<p style='text-indent:50pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3.3. Reference Size: total number of transactions in the input file (total size of the reference list);</span></p>"
					+ "<p style='text-indent:50pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3.4. Selected Reference Size: number of transactions selected in the input file (total size of the input list);</span></p>"
					+ "<p style='text-indent:50pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3.5. Reference Size: total number of transactions in the input file (total size of the reference list);</span></p>"
					+ "<p style='text-indent:50pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3.6. P-Value : correction method: 0 for none; any number < 0 for FDR method; and, any number > 0 for the number of permutations to correct p-values with the permutations method;</span></p>"
		        	+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>4. Mark the email notification checkbox if you would like to receive an email message notifying the completion of your job;</span></p>"
		        	+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>5. Click the <b>Submit</b> button.</span></p>"
					+ "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>After clicking the <b>Submit</b> button, you can monitor the status of the jobs you have submitted. When a task finishes, the job status tree shows the result node. So, you can download the task output by clicking on this link. You can also retrieve the output of your jobs using the File Explorer application. You can repeat the process and submit other jobs by clicking in the <b>New Job</b> button; each new job submitted will appear in a separate tab.</span></p>"; 
		
		return text;
	}

}