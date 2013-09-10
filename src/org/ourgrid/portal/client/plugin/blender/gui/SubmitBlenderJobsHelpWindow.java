package org.ourgrid.portal.client.plugin.blender.gui;

import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsHelpWindow;

public class SubmitBlenderJobsHelpWindow extends PluginSubmitJobsHelpWindow {
	
	public SubmitBlenderJobsHelpWindow(String message) {
		super(message);
	}
	@Override
	protected String getHelpText(){
		
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application allows the submission of jobs for rendering files using <b><a href=\"http://www.blender.org/\" target=\"_blank\">Blender</a></b>, the processing is split in several tasks that are executed in parallel in the <b><a href=\"http://www.ourgrid.org/\" target=\"_blank\">OurGrid</a></b> Community Grid.</span></p>" 
					+ "<p><span style='line-height:10% ;font-size:10.0pt ; font-family:\"Arial\"'>The data required for the processing are:</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. <b>Output type selection</b>: output extension;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. <b>Scenes number</b>: number of frames per task;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3. <b>Start frame number</b>: render from that frame;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>4. <b>End frame number</b>: render until that frame;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>5. <b>Email notification</b>: email notification of final execution;</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>6. <b>Input file</b>: Blender file to render.</span></p>";

		return text;
	}

}