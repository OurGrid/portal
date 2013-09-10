package org.ourgrid.portal.client.common;

import java.io.Serializable;

public class CaptchaChallenge implements Serializable {

	private static final long serialVersionUID = 9157521464129204541L;
	
	private String image_url;
	private String answer;
	
	public CaptchaChallenge(){
		this("", "");
	}
	
	public CaptchaChallenge(String url, String answer) {
		this.image_url = url;
		this.answer = answer;
	}
	
	public String getImageURL(){
		return this.image_url;
	}
	
	public String getAnswer(){
		return this.answer;
	}
}
