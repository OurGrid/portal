package org.ourgrid.portal.client.plugin.genecodis.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class AnalysisTypeComboBoxModel extends BaseModel {

	private static final long serialVersionUID = -1073781619729074056L;

	public AnalysisTypeComboBoxModel(String type, Integer value) {
		set("analysisType", type);
		set("value", value);
	}
	
	public String getAnalysisType() {
		return get("analysisType");
	}
	
	public void setAnalysisType(String type) {
		set("analysisType", type);
	}
	
	public Integer getAnalysisValue() {
		return get("value");
	}
	
	public void setAnalysisValue(Integer value) {
		set("value", value);
	}
}