package org.ourgrid.portal.test.client.common;

import java.io.FileWriter;
import java.io.IOException;

import org.ourgrid.portal.server.logic.executors.common.CaptchaGeneratorExecutor;
import org.ourgrid.portal.server.util.CaptchaUtil;
import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class RecoverPasswordSeleniumTest extends SeleneseTestCase {
	
	private DefaultSelenium selenium;
	
	private String USER_LOGIN = "testRecover";
	private String USER_PASSWORD = "testRecover";
	private String USER_EMAIL = "testRecover@gmail.com";
	private String FALSE_USER_EMAIL = "mgmail.com";
	private String BAD_USER_EMAIL = "maaaa@gmail.com";
	private String GOOD_CAPTCHA = "waterloo arctic";
	
	public void setUp() throws Exception {

		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
	private void configureBuildTest(String isBuildTest) {
		
		try {
			FileWriter file = new FileWriter("./src/org/ourgrid/portal/test/client/build_test.txt");
			file.write(isBuildTest);
			file.close();
		} catch (IOException e) {
		}
	}
	
	public void testRecoverPasswordSeleniumSucess() throws Exception {

		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		registerUser();
		
		selenium.click("recoverPasswButtonIdLogin");
		
		RecoverPasswordTextSelenium();
		
		selenium.type("emailIdRecoverPassw-input", USER_EMAIL);
		selenium.click("recoverButtonIdRecoverPassw");
		
		waitForTest("Password recovered successfully. Please check your e-mail.");
		
		verifyTrue(selenium.isTextPresent("Password recovered successfully. Please check your e-mail."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
		configureBuildTest("false");
	}
	
	public void testRecoverPasswordSeleniumFail() throws Exception {

		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.click("recoverPasswButtonIdLogin");
		
		RecoverPasswordTextSelenium();
		
		selenium.type("emailIdRecoverPassw-input", BAD_USER_EMAIL);
		selenium.click("recoverButtonIdRecoverPassw");
		
		waitForTest("Error on recovering password.");
		
		verifyTrue(selenium.isTextPresent("Error on recovering password."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}
	
	public void testRecoverPasswordSeleniumFailMail() throws Exception {

		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.click("recoverPasswButtonIdLogin");
		
		RecoverPasswordTextSelenium();
		
		selenium.type("emailIdRecoverPassw-input", FALSE_USER_EMAIL);
		selenium.click("recoverButtonIdRecoverPassw");
		
		waitForTest("Your email is not valid.");
		
		verifyTrue(selenium.isTextPresent("Your email is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}

	private void RecoverPasswordTextSelenium() throws Exception {
		verifyTrue(selenium.isTextPresent("Email:"));
		verifyTrue(selenium.isTextPresent("Recover"));
		verifyTrue(selenium.isTextPresent("Cancel"));
	}
	
	private void registerUser() throws InterruptedException {
		
		selenium.click("registerButtonIdLogin");
		selenium.type("loginIdRegister-input", USER_LOGIN);
		selenium.type("passwordIdRegister-input", USER_PASSWORD);
		selenium.type("confirmPasswordIdRegister-input", USER_PASSWORD);
		selenium.type("emailIdRegister-input", USER_EMAIL);
		selenium.type("captchaIdRegister-input", GOOD_CAPTCHA);
		selenium.click("submitButtonIdRegister");
		waitForTest("Ok");
		selenium.click("//button[text()='Ok']");
	}
	
	
	private void waitForTest(String txt) throws InterruptedException{
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}

}
