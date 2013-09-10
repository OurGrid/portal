package org.ourgrid.portal.client.plugin.blender.to.service;

import java.util.List;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.blender.to.model.BlenderInputFileTO;

public class BlenderJobSubmissionTO extends JobSubmissionTO {

	private static final long serialVersionUID = -6611551079605888649L;

	private List<BlenderInputFileTO> inputFile;
	
	public void setInputFileList(List<BlenderInputFileTO> listInput) {
		this.inputFile = listInput;
	}
	
	public List<BlenderInputFileTO> getInputFileList() {
		return inputFile;
	}
	
}