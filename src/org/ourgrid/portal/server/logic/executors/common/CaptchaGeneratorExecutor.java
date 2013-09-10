package org.ourgrid.portal.server.logic.executors.common;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.CaptchaChallenge;
import org.ourgrid.portal.client.common.to.response.CaptchaGeneratorResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.logic.executors.ExecutorIF;
import org.ourgrid.portal.server.util.OurgridPortalProperties;
import org.ourgrid.worker.utils.RandomNumberUtil;

public class CaptchaGeneratorExecutor implements ExecutorIF {

	private String [] answers  = {"sushled", "friendship cases", "birds foundations",
			"behavior present", "waterloo arctic", "whisper utopia", "fierce civilian",
			"circus pressure", "carnival hands", "house stand", "narrow sunshine",
			"destiny listen", "mercy shine", "remind float", "couple seventh",
			"lessons static", "discover grace", "still bricks", "happy flight",
			"spirit noise"};
	private static final String CAPTCHA_FILE_PATTERN = "captcha";
	private static final String JPG = ".jpg";
	private static final String images_folder = "resources" + "/" + "images" + "/" + "captchas"; 
	
	public List<CaptchaChallenge> challenges;
	
	public CaptchaGeneratorExecutor() {
		this.challenges = new LinkedList<CaptchaChallenge>();
		for (int i = 0; i < answers.length; i++){
			String url = images_folder + "/" + CAPTCHA_FILE_PATTERN + i + JPG;
			challenges.add(new CaptchaChallenge(url, answers[i]));
		}
	}
	
	public ResponseTO execute(ServiceTO serviceTO) {
		
		CaptchaGeneratorResponseTO captchaGeneratorResponseTO = new CaptchaGeneratorResponseTO();
		String buildTest = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.BUILD_TEST);
		Boolean isBuildTest = Boolean.valueOf(buildTest);
		
		captchaGeneratorResponseTO.setCaptchaChallenge(getNewChallenge(isBuildTest));
		return captchaGeneratorResponseTO ;
	}
	
	public CaptchaChallenge getNewChallenge(boolean isBuildTest){
		int challenge_index = 4;
		if(!isBuildTest){
			challenge_index = (int) Math.floor(Math.abs(RandomNumberUtil.getRandom()) % this.answers.length);
		}
		return this.challenges.get(challenge_index);
	}

	public void logTransaction(ServiceTO serviceTO) {}
}