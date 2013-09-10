package org.ourgrid.portal.client.plugin.genecodis.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class StatisticalTestTypeComboBoxModel extends BaseModel {

	private static final long serialVersionUID = -1073781619729074056L;

	public StatisticalTestTypeComboBoxModel(String type, Integer value) {
		set("statisticalTestType", type);
		set("value", value);
	}
	
	public String getStatisticalTestType() {
		return get("statisticalTestType");
	}
	
	public void setStatisticalTestType(String type) {
		set("statisticalTestType", type);
	}
	
	public Integer getStatisticalTestValue() {
		return get("value");
	}
	
	public void setStatisticalTestValue(Integer value) {
		set("value", value);
	}
	
	
}
