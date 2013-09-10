package org.ourgrid.portal.client.plugin.reservatorios.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class SimulationPeriodComboBox extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1553650899576582660L;

	public SimulationPeriodComboBox(String type) {
		set("month", type);
	}
	
	public String getMonth() {
		return get("month");
	}
	
	public void setMonth(String type) {
		set("month", type);
	}

}
