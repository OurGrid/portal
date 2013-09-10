package org.ourgrid.portal.client.common.to.response;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.FileTO;


public class FileExplorerResponseTO extends ResponseTO implements Serializable {
	
	private static final long serialVersionUID = 3364738242452557570L;

	private FileTO root;
	private FileTO rootTmp;
	private boolean isQuotaExed;
	private Double quotaUsed;
	private Double quotaPercentage;
	private String quotaExceeded;

	private List<AbstractRequest> jobsRequestList;

	private boolean checkQuota;

	public FileTO getFileRoot() {
		return root;
	}

	public FileTO getFileRootTmp() {
		return rootTmp;
	}

	public void setFileRootTmp(FileTO rootTmp) {
		this.rootTmp = rootTmp;
	}

	public FileExplorerResponseTO() {
		super();
	}

	public void setFileRoot(FileTO folderTO) {
		this.root = folderTO;
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

	public void setCheckQuota(boolean checkQuota) {
		this.checkQuota = checkQuota;
	}
	
	public boolean checkQuota(){
		return checkQuota;
	}

}