package org.ourgrid.portal.client.plugin.blender.gui.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class InputFileGridModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8900471947954157033L;

	public InputFileGridModel(String inputName, Long uploadNameId, Integer scenesNumber, String outputType, String outputExt, Integer startFrame, Integer endFrame) {
		set("name", inputName);
		set("upload", uploadNameId);
		set("scenes", scenesNumber);
		set("output", outputType);
		set("ext", outputExt);
		set("start", startFrame);
		set("end", endFrame);
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
	
	public Integer getScenesNumber() {
		return get("scenes");
	}
	
	public void setScenesNumber(String scenesNumber) {
		set("scenes", new Integer(scenesNumber));
	}
	
	public String getOutputTypeFile() {
		return get("output");
	}
	
	public void setOutputTypeFile(String outputType) {
		set("output", outputType);
	}
	
	public String getOutputExtFile() {
		return get("ext");
	}
	
	public void setOutputExtFile(String outputType) {
		set("ext", outputType);
	}
	
	public Integer getStartFrame() {
		return get("start");
	}
	
	public void setStartFrame(String start) {
		set("start", start);
	}
	
	public Integer getEndFrame() {
		return get("end");
	}
	
	public void setEndFrame(String end) {
		set("end", end);
	}
	
}
