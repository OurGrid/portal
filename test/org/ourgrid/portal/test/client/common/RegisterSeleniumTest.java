package org.ourgrid.portal.test.client.common;

import java.io.FileWriter;
import java.io.IOException;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class RegisterSeleniumTest extends SeleneseTestCase {
	
	private DefaultSelenium selenium;
	
	private final String GOOD_LOGIN = "ourgrid";
	private final String BAD_LOGIN = "ourgrid/";
	
	private final String GOOD_PASSW = "aze";
	private final String BAD_PASSW = "azer";
	
	private final String GOOD_MAIL = "ourgrid@a.com";
	private final String BAD_MAIL = "ourgrid@a.a";
	
	private final String BAD_CAPTCHA = "test";
	private final String GOOD_CAPTCHA = "waterloo arctic";
	
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

	public void testRegisterSeleniumFailLogin() throws Exception {
		
		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		selenium.click("registerButtonIdLogin");
		
		Thread.sleep(1000);
		
		registerTextSelenium();
		
		selenium.type("loginIdRegister-input", BAD_LOGIN);
		selenium.type("passwordIdRegister-input", GOOD_PASSW);
		selenium.type("confirmPasswordIdRegister-input", GOOD_PASSW);
		selenium.type("emailIdRegister-input", GOOD_MAIL);
		
		selenium.click("submitButtonIdRegister");
		
		waitForTest("Your login is not valid. Please, insert a alphanumeric characters.");
		
		verifyTrue(selenium.isTextPresent("Your login is not valid. Please, insert a alphanumeric characters."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.click("//button[text()='Ok']");
		
		selenium.click("submitButtonIdRegister");
		loginTextSelenium();
		
		selenium.close();
		configureBuildTest("false");

	}
	
	public void testRegisterSeleniumFailPassw() throws Exception {
	
		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		selenium.click("registerButtonIdLogin");
		
		registerTextSelenium();
		
		selenium.type("loginIdRegister-input", GOOD_LOGIN);
		selenium.type("passwordIdRegister-input", GOOD_PASSW);
		selenium.type("confirmPasswordIdRegister-input", BAD_PASSW);
		selenium.type("emailIdRegister-input", GOOD_MAIL);
		
		selenium.click("submitButtonIdRegister");

		waitForTest("The passwords doesn't match!");
		
		verifyTrue(selenium.isTextPresent("The passwords doesn't match!"));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.click("//button[text()='Ok']");
		
		selenium.click("registerButtonIdLogin");
		loginTextSelenium();
		
		selenium.close();
		configureBuildTest("false");

	}
	
	public void testRegisterSeleniumFailMail() throws Exception {
		
		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		selenium.click("registerButtonIdLogin");
		
		Thread.sleep(1000);
		
		registerTextSelenium();
		
		selenium.type("loginIdRegister-input", GOOD_LOGIN);
		selenium.type("passwordIdRegister-input", GOOD_PASSW);
		selenium.type("confirmPasswordIdRegister-input", GOOD_PASSW);
		selenium.type("emailIdRegister-input", BAD_MAIL);
		
		selenium.click("submitButtonIdRegister");
		
		waitForTest("Your email is not valid.");
		
		verifyTrue(selenium.isTextPresent("Your email is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.click("//button[text()='Ok']");
		
		selenium.click("submitButtonIdRegister");
		loginTextSelenium();

		selenium.close();
		configureBuildTest("false");

	}
	
	public void testRegisterSeleniumFailCaptcha() throws Exception {
		
		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		selenium.click("registerButtonIdLogin");
		
		Thread.sleep(1000);
		
		registerTextSelenium();
		
		selenium.type("loginIdRegister-input", GOOD_LOGIN);
		selenium.type("passwordIdRegister-input", GOOD_PASSW);
		selenium.type("confirmPasswordIdRegister-input", GOOD_PASSW);
		selenium.type("emailIdRegister-input", GOOD_MAIL);
		selenium.type("captchaIdRegister-input", BAD_CAPTCHA);
		
		selenium.click("submitButtonIdRegister");
		
		waitForTest("The given words don't match.");
		
		verifyTrue(selenium.isTextPresent("The given words don't match."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.click("//button[text()='Ok']");
		
		selenium.click("registerButtonIdLogin");
		
		loginTextSelenium();
		
		selenium.close();
		configureBuildTest("false");

	}
	
public void testRegisterSeleniumSucess() throws Exception {

		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		selenium.click("registerButtonIdLogin");
		
		Thread.sleep(1000);
		
		registerTextSelenium();
		
		selenium.type("loginIdRegister-input", GOOD_LOGIN);
		selenium.type("passwordIdRegister-input", GOOD_PASSW);
		selenium.type("confirmPasswordIdRegister-input", GOOD_PASSW);
		selenium.type("emailIdRegister-input", GOOD_MAIL);
		selenium.type("captchaIdRegister-input", GOOD_CAPTCHA);
		
		selenium.click("submitButtonIdRegister");
		
		waitForTest("");
		
		verifyTrue(selenium.isTextPresent(""));
		
		selenium.close();
		configureBuildTest("false");
	}
	
	
	private void registerTextSelenium() throws Exception {
		
		verifyTrue(selenium.isTextPresent("Login:"));
		verifyTrue(selenium.isTextPresent("Password:"));
		verifyTrue(selenium.isTextPresent("Confirm:"));
		verifyTrue(selenium.isTextPresent("Email:"));
		verifyTrue(selenium.isTextPresent("Captcha:"));
		verifyTrue(selenium.isTextPresent("Submit"));
		verifyTrue(selenium.isTextPresent("Cancel"));
	}
	
	private void loginTextSelenium() throws Exception {
		
		verifyTrue(selenium.isTextPresent("Login:"));
		verifyTrue(selenium.isTextPresent("Password:"));
		verifyTrue(selenium.isTextPresent("Submit"));
		verifyTrue(selenium.isTextPresent("Register"));
	}
	
	private void waitForTest(String txt) throws InterruptedException{
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}

