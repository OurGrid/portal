package org.ourgrid.portal.client.common.to.response;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.FileTO;

public class CompressFilesResponseTO extends ResponseTO implements Serializable{

	private static final long serialVersionUID = -2197534586092940620L;

	private FileTO file;
	
	private boolean isQuotaExed;
	
	private Double quotaUsed;
	
	private String quotaExceeded;

	private Double quotaPercentage;
	
	private List<AbstractRequest> jobsRequestList;
	
	public void setFile(FileTO file) {
		this.file = file;
	}

	public FileTO getFile() {
		return file;
	}
	
	public boolean isQuotaExed() {
		return isQuotaExed;
	}

	public void setQuotaExed(boolean isQuotaExed) {
		this.isQuotaExed = isQuotaExed;
	}

	public Double getQuotaUsed() {
		return quotaUsed;
	}

	public void setQuotaUsed(Double storageUsed) {
		this.quotaUsed = storageUsed;
	}

	public String getQuotaExceeded() {
		return quotaExceeded;
	}
	
	public void setQuotaExceeded(String storageExceededFormated) {
		this.quotaExceeded = storageExceededFormated;
	}

	public Double getQuotaPercentage() {
		return quotaPercentage;
	}

	public void setQuotaPercentage(Double quotaPercentage) {
		this.quotaPercentage = quotaPercentage;
	}
	
	public List<AbstractRequest> getJobsRequestList() {
		return jobsRequestList;
	}

	public void setJobsRequestList(List<AbstractRequest> jobsRequestList) {
		this.jobsRequestList = jobsRequestList;
	}
}