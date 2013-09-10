package org.ourgrid.portal.server.logic.executors.to.response;

import org.ourgrid.portal.client.common.to.response.ResponseTO;

public class CheckQuotaResponseTO extends ResponseTO {

	private static final long serialVersionUID = 642171175943526359L;
	
	private boolean isQuotaExed;
	
	private Double quotaUsed;
	
	private String quotaExceeded;

	private Double quotaPercentage;
	
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

	public boolean getQuotaExed() {
		return isQuotaExed;
	}
}