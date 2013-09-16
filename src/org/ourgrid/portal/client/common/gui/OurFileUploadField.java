package org.ourgrid.portal.client.common.gui;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.util.OurFileUploadEventListener;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FileUpload;

public class OurFileUploadField extends FileUpload {

	private List<OurFileUploadEventListener> listeners = new LinkedList<OurFileUploadEventListener>();
	
	public OurFileUploadField() {
		super();
		sinkEvents(Event.ONCHANGE | Event.ONKEYUP);
	}
	
	public void addListener(OurFileUploadEventListener listener) {
		this.listeners.add(listener);
	}
	
	public void onBrowserEvent(Event event) {

		for (OurFileUploadEventListener listener : listeners) {
			listener.onEvent(event);
		}

		super.onBrowserEvent(event);
		
	}
	
	public void setName(String name){
		super.setName(name);
	}
	
	@Override
	public String getFilename() {
		String fullFileName = super.getFilename();
		if (fullFileName.indexOf('\\') >= 0) {
			return fullFileName.substring(fullFileName.lastIndexOf('\\') + 1);
		}
		if (fullFileName.indexOf('/') >= 0) {
			return fullFileName.substring(fullFileName.lastIndexOf('/') + 1);
		}
		return fullFileName;
	}
}