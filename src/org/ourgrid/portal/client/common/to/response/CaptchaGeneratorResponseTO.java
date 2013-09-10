package org.ourgrid.portal.client.common.to.response;

import org.ourgrid.portal.client.common.CaptchaChallenge;


public class CaptchaGeneratorResponseTO extends ResponseTO {
	
	private static final long serialVersionUID = 3769335196462220468L;
	
	public CaptchaChallenge captchaChallenge;
	
	public CaptchaGeneratorResponseTO() {
		super();
	}

	public CaptchaChallenge getCaptchaChallenge() {
		return captchaChallenge;
	}

	public void setCaptchaChallenge(CaptchaChallenge captchaChallenge) {
		this.captchaChallenge = captchaChallenge;
	}
}
