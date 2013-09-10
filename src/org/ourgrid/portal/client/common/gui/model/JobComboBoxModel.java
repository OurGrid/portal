package org.ourgrid.portal.client.common.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class JobComboBoxModel extends BaseModel {

	private static final long serialVersionUID = -1073781619729074056L;

	public JobComboBoxModel(String jobName) {
		set("jobName", jobName);
	}
	
	public String getJobName() {
		return get("jobName");
	}
	
	public void setJobName(String jobName) {
		set("jobName", jobName);
	}
}