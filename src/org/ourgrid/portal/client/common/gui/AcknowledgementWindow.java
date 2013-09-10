package org.ourgrid.portal.client.common.gui;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;

public class AcknowledgementWindow extends Window {
	
private final ContentPanel panel = new ContentPanel();
	
	public AcknowledgementWindow() {
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
		this.setSize(500, 265);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(false);
		this.setHeaderVisible(true);
		this.setMinimizable(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading("Acknowledgements");
		this.setClosable(true);
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
//		OurGridPortal.removeWindow(this);
	}
	
	private String getHelpText(){
		
		String text = "<p><span style='font-size:10.0pt;font-family:\"Arial\"'>The management of the OurGrid Community core services and the applications in this "
					+ "portal are partly supported by the <b><a href=\"http://degisco.eu/\" target=\"_blank\">DEGISCO</a></b>, <b><a href=\"http://www.gisela-grid.eu/\" target=\"_blank\">GISELA</a></b> and <b><a href=\"http://seghidro.lsd.ufcg.edu.br/\" target=\"_blank\">SegHidro</a></b> projects. "
					+ "<br></br>"
					+ "The work leading to these results has received funding from the European Union Seventh Framework Programme (FP7/2007-2013) "
					+ "under grant agreements n° 261556 and n° 261487, from CNPq/Brazil under grant agreement n° 305858/2010-6, FINEP/Brazil under " 
					+ "grant agreement n° 1861/04, and The Brazilian Minister of Science and Technology under grant agreement n° 01.0049.00/2007. "
                    + "<br></br>"
					+ "The experts of the <b><a href=\"http://desktopgridfederation.org/\" target=\"_blank\">International Desktop Grid Federation</a></b> provide further support for the OurGrid Community,"
                    + " its applications, and its integration into the <b><a href=\"http://degisco.eu/\" target=\"_blank\">DEGISCO</a></b> infrastructure.</span></p>"; 

		return text;
	}
}

