package org.ourgrid.portal.client.plugin.genecodis.to.service;

import java.util.List;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;

public class GenecodisJobSubmissionTO extends JobSubmissionTO {

	private static final long serialVersionUID = -6611551079605888649L;

	private List<GenecodisInputFileTO> inputFile;
	
	private Long 	uploadId;
	private String 	inputFileName;
	private String 	exeFileName;
	
	private List<GenecodisInputFileTO> inputsFile;

	public List<GenecodisInputFileTO> getInputsFileTO() {
		return inputsFile;
	}

	public void setInputsFile(List<GenecodisInputFileTO> inputsFile) {
		this.inputsFile = inputsFile;
	}

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getExeFileName() {
		return exeFileName;
	}

	public void setExeFileName(String exeFileName) {
		this.exeFileName = exeFileName;
	}

	public void setInputFile(List<GenecodisInputFileTO> input) {
		this.inputFile = input;
	}
	
	public List<GenecodisInputFileTO> getInputFile() {
		return inputFile;
	}

}