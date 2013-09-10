package org.ourgrid.portal.client.plugin.genecodis.gui.model;

import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;

import com.extjs.gxt.ui.client.data.BaseModel;

public class InputFileGridModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8900471947954157033L;
	
	private GenecodisInputFileTO genecodisInputFileTO;

	public GenecodisInputFileTO getGenecodisInputFileTO() {
		return genecodisInputFileTO;
	}

	public void setGenecodisInputFileTO(GenecodisInputFileTO genecodisInputFileTO) {
		this.genecodisInputFileTO = genecodisInputFileTO;
	}

	public InputFileGridModel(String fileName, Long uploadNameId, Integer suportNumber, Integer analysisType, Integer suportForRandon,
			Integer referenceSize, Integer selectedReferenceSize, Integer statisticalTestType, Integer pValue) {
		set("name", fileName);
		set("upload", uploadNameId);
		set("suport", suportNumber);
		set("analysis type", analysisType);
		set("suport for randon", suportForRandon);
		set("reference size", referenceSize);
		set("selected reference size", selectedReferenceSize);
		set("statistical test", statisticalTestType);
		set("p-value", pValue);
	}
	
	public String getName() {
		return get("name");
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public Long getUploadId() {
		return get("upload");
	}
	
	public void setUploadId(Long uploadId) {
		set("upload", uploadId);
	}
	
	public Integer getSuportNumber() {
		return get("suport");
	}
	
	public void setSuportNumber(String suportNumber) {
		set("suport", new Integer(suportNumber));
	}
	
	public String getAnalysisType() {
		return get("analysis type");
	}
	
	public void setAnalysisType(String analysisType) {
		set("analysis type", analysisType);
	}
	
	public String getSuportForRandon() {
		return get("suport for randon");
	}
	
	public void setSuportForRandon(String suportForRandon) {
		set("suport for randon", suportForRandon);
	}
	
	public Integer getRerenceSize() {
		return get("reference size");
	}
	
	public void setReferenceSize(String referenceSize) {
		set("reference size", referenceSize);
	}
	
	public Integer getSetSelectedRefSize() {
		return get("selected reference size");
	}
	
	public void setSelectedRefSize(String selectedRefSize) {
		set("selected reference size", selectedRefSize);
	}
	
	public String getStatisticalTestType() {
		return get("statistical test");
	}
	
	public void setStatisticalTestType(String statisticalTestType ) {
		set("statistical test", statisticalTestType);
	}
	
	public String getPValue() {
		return get("p-value");
	}
	
	public void setPValue(String pValue) {
		set("p-value", pValue);
	}
}
