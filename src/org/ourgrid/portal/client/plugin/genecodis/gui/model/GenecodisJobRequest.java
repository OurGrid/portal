package org.ourgrid.portal.client.plugin.genecodis.gui.model;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;

public class GenecodisJobRequest extends AbstractRequest implements Serializable {

	private static final long serialVersionUID = 2287719427433948796L;
	
	private List<GenecodisInputFileTO> inputFiles;

	public GenecodisJobRequest() {
		super();
	}
	
	public GenecodisJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			String inputFileName, List<GenecodisInputFileTO> inputFiles) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.GENECODIS_JOB);
		
		this.inputFiles = inputFiles;
	}

	public List<GenecodisInputFileTO> getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(List<GenecodisInputFileTO> inputFiles) {
		this.inputFiles = inputFiles;
	}
	
}