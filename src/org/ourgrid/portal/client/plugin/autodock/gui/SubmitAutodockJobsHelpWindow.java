package org.ourgrid.portal.client.plugin.autodock.gui;

import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsHelpWindow;

public class SubmitAutodockJobsHelpWindow extends PluginSubmitJobsHelpWindow {
	
	public SubmitAutodockJobsHelpWindow(String message) {
		super(message);
	}
	@Override
	protected String getHelpText(){
		
		String text = "<p><span style='font-size:10.0pt ; font-family:\"Arial\"'>This application allows the submission of jobs using <b><a href=\"http://autodock.scripps.edu/\" target=\"_blank\">Autodock</a></b>, the processing is split in several tasks that are executed in parallel in the <b><a href=\"http://www.ourgrid.org/\" target=\"_blank\">OurGrid</a></b> Community Grid.</span></p>" 
					+ "<p><span style='line-height:10% ;font-size:10.0pt ; font-family:\"Arial\"'>The data required for the processing are:</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>1. <b>Macromolecule Rigid File</b>: receptor file used by AutoDock (.pdbqt);</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>2. <b>Macromolecule Flexible File</b>: receptor file used by AutoDock (.pdbqt);</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>3. <b>Grid Parameters File</b>: tells AutoGrid the types of maps to compute, the location and extent of those maps and specifies pair-wise potential energy parameters (.gpf);</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>4. <b>Ligand File</b>: have partial atomic charges for each atom (.pdbqt);</span></p>"
					+ "<p style='text-indent:35.4pt'><span style='font-size:10.0pt ; font-family:\"Arial\"'>5. <b>Docking Parameters File</b>: tells AutoDock which map files to use, the ligand molecule to move, what its center and number of torsions are, where to start the ligand, which docking algorithm to use and how many runs to do (.dpf).</span></p>";

		return text;
	}

}