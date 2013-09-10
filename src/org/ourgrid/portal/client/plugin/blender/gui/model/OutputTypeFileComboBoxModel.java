package org.ourgrid.portal.client.plugin.blender.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class OutputTypeFileComboBoxModel extends BaseModel {

	private static final long serialVersionUID = -1073781619729074056L;

	public OutputTypeFileComboBoxModel(String type) {
		set("type", type);
	}
	
	public String getOutputTypeFile() {
		return get("type");
	}
	
	public void setOutputTypeFile(String type) {
		set("type", type);
	}
}