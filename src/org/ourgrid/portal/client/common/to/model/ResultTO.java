package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;

public class ResultTO extends AbstractTreeNodeTO implements Serializable {
	
	public static final String typeValue = "ResultTO";
	
	private static final long serialVersionUID = 3717603854500577313L;
	
	private static final String url = "url";
	private static final String text = "text";
	
	public ResultTO() {
		super();
		
		setType(typeValue);
		setDescription("");
	}
	
	public String getUrl() {
		return get(ResultTO.url);
	}
	
	public String getText() {
		return get(ResultTO.text);
	}

	public void setUrl(String url) {
		set(ResultTO.url, url);
	}
	
	public void setText(String text) {
		set(ResultTO.text, text);
	}
	
	public String toString() {
		return "Result : [ " + this.getText() + " ]";
	}
}