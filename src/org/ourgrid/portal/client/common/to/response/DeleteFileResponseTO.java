package org.ourgrid.portal.client.common.to.response;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class DeleteFileResponseTO extends ResponseTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<FileTO> listFiles;

	private Double quotaPercentage;

	private boolean isQuotaExed;
	
	public DeleteFileResponseTO() {
		super();
	}

	public List<FileTO> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<FileTO> listFiles) {
		this.listFiles = listFiles;
	}

	public Double getQuotaPercentage() {
		return quotaPercentage;
	}
	
	public void setQuotaPercentage(Double quotaPercentage){
		this.quotaPercentage = quotaPercentage;
	}

	public boolean isQuotaExed() {
		return isQuotaExed;
	}
	
	public void setIsQuotaExed(boolean isQuotaExed){
		this.isQuotaExed = isQuotaExed;
	}


}