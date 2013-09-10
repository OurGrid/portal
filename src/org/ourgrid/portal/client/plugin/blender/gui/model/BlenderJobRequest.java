package org.ourgrid.portal.client.plugin.blender.gui.model;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.blender.to.model.BlenderInputFileTO;

public class BlenderJobRequest extends AbstractRequest implements Serializable {

	private static final long serialVersionUID = 2287719427433948796L;
	
	private List<BlenderInputFileTO> inputFiles;
	
	public BlenderJobRequest() {
		super();
	}
	
	public BlenderJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			List<BlenderInputFileTO> inputFiles) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.BLENDER_JOB);
		
		this.inputFiles = inputFiles;
	}

	public List<BlenderInputFileTO> getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(List<BlenderInputFileTO> inputFiles) {
		this.inputFiles = inputFiles;
	}
	
}