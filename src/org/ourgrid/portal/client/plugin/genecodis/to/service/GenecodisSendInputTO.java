package org.ourgrid.portal.client.plugin.genecodis.to.service;

import org.ourgrid.portal.client.common.to.service.ServiceTO;

public class GenecodisSendInputTO extends ServiceTO {

	private static final long serialVersionUID = -3869294671658209607L;

	public static final String typeValue = "ResultTO";

	private boolean isAllParameters;
	private String inputFileName;
	private Integer genesSupport;
	private Integer analysisType;
	private Integer supportForRandom;
	private Integer referenceSize;
	private Integer selectedRefSize;
	private Integer statisticalTest;
	private Integer correctionMethod;
	
	public boolean isAllParameters() {
		return isAllParameters;
	}

	public void setAllParameters(boolean isAllParameters) {
		this.isAllParameters = isAllParameters;
	}
	
	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	
	public Integer getGenesSupport() {
		return genesSupport;
	}

	public void setGenesSupport(Integer genesSupport) {
		this.genesSupport = genesSupport;
	}

	public Integer getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(Integer analysisType) {
		this.analysisType = analysisType;
	}

	public Integer getSupportForRandom() {
		return supportForRandom;
	}

	public void setSupportForRandom(Integer supportForRandom) {
		this.supportForRandom = supportForRandom;
	}

	public Integer getReferenceSize() {
		return referenceSize;
	}

	public void setReferenceSize(Integer referenceSize) {
		this.referenceSize = referenceSize;
	}

	public Integer getSelectedRefSize() {
		return selectedRefSize;
	}

	public void setSelectedRefSize(Integer selectedRefSize) {
		this.selectedRefSize = selectedRefSize;
	}

	public Integer getStatisticalTest() {
		return statisticalTest;
	}

	public void setStatisticalTest(Integer statisticalTest) {
		this.statisticalTest = statisticalTest;
	}

	public Integer getCorrectionMethod() {
		return correctionMethod;
	}

	public void setCorrectionMethod(Integer correctionMethod) {
		this.correctionMethod = correctionMethod;
	}
}