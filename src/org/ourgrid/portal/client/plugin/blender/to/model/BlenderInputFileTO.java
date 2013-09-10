package org.ourgrid.portal.client.plugin.blender.to.model;

import java.io.Serializable;

public class BlenderInputFileTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String typeValue = "ResultTO";
	
	private String name;
	private Integer scenesNumber;
	private String outputType;
	private String outputExt;
	private Integer start;
	private Integer end;
	private Long uploadId;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getScenesNumber() {
		return scenesNumber;
	}

	public void setScenesNumber(Integer scenesNumber) {
		this.scenesNumber = scenesNumber;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public String getOutputExt() {
		return outputExt;
	}

	public void setOutputExt(String outputExt) {
		this.outputExt = outputExt;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Long getUploadId(){
		return uploadId;
	}
	
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}
}