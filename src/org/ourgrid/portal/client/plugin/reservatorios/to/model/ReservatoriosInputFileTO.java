package org.ourgrid.portal.client.plugin.reservatorios.to.model;

import java.io.Serializable;

public class ReservatoriosInputFileTO implements Serializable {

	private static final long serialVersionUID = 2613310276462044779L;
	
	private boolean complete;
	
	private Integer startMonth;
	private Integer endMonth;
	
	private boolean readPesFromFile;
	private Double  dry;
	private Double  normal;
	private Double  rainy;
	
	private Long    uploadID;

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	public Double getDry() {
		return dry;
	}

	public void setDry(Double dry) {
		this.dry = dry;
	}

	public Double getNormal() {
		return normal;
	}

	public void setNormal(Double normal) {
		this.normal = normal;
	}

	public Double getRainy() {
		return rainy;
	}

	public boolean getComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void setRainy(Double rainy) {
		this.rainy = rainy;
	}

	public Long getUploadID() {
		return uploadID;
	}

	public void setUploadID(Long uploadID) {
		this.uploadID = uploadID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isReadPesFromFile() {
		return readPesFromFile;
	}

	public void setReadPesFromFile(boolean readPesFromFile) {
		this.readPesFromFile = readPesFromFile;
	}
	
}
