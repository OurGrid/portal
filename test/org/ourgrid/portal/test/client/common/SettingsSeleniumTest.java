package org.ourgrid.portal.test.client.common;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class SettingsSeleniumTest extends SeleneseTestCase {
	
	private DefaultSelenium selenium;
	
	private final String LOGIN = "gustavo";
	private final String PASSW = "gus";
	private final String BAD_PASSW = "123";
	private final String MAIL = "gugajansen@gmail.com";
	private final String BAD_MAIL = "gugajansengmail.com";
	
	public void setUp() throws Exception {
		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
	public void testSettingsSeleniumFailPassw() throws Exception {
	
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", LOGIN);
		selenium.type("passwordIdLogin-input", PASSW);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("settingsItem");

		settingsTextSelenium();
		
		selenium.type("currentPasswIdSettings-input", BAD_PASSW);
		selenium.click("submitButtonIdSettings");
		
		waitForTest("Invalid Password.");
		
		verifyTrue(selenium.isTextPresent("Invalid Password."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}

	public void testSettingsSeleniumFailNewPassw() throws Exception {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", LOGIN);
		selenium.type("passwordIdLogin-input", PASSW);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("settingsItem");

		settingsTextSelenium();
		
		selenium.type("currentPasswIdSettings-input", PASSW);
		selenium.type("newPasswIdSettings-input", PASSW);
		selenium.type("confirmNewPasswIdSettings-input", BAD_PASSW);
		selenium.click("submitButtonIdSettings");

		waitForTest("The news passwords doesn't match!");
		
		verifyTrue(selenium.isTextPresent("The news passwords doesn't match!"));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}
	
	public void testSettingsSeleniumFailMail() throws Exception {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", LOGIN);
		selenium.type("passwordIdLogin-input", PASSW);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("settingsItem");

		settingsTextSelenium();
		
		selenium.type("currentPasswIdSettings-input", PASSW);
		selenium.type("newPasswIdSettings-input", PASSW);
		selenium.type("confirmNewPasswIdSettings-input", PASSW);
		selenium.type("emailIdSettings-input", BAD_MAIL);
		selenium.click("submitButtonIdSettings");
		
		waitForTest("Your new email is not valid!");
		
		verifyTrue(selenium.isTextPresent("Your new email is not valid!"));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}
	
	public void testSettingsSeleniumSucess() throws Exception {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", LOGIN);
		selenium.type("passwordIdLogin-input", PASSW);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("settingsItem");

		settingsTextSelenium();
		
		selenium.type("currentPasswIdSettings-input", PASSW);
		selenium.type("newPasswIdSettings-input", PASSW);
		selenium.type("confirmNewPasswIdSettings-input", PASSW);
		selenium.type("emailIdSettings-input", MAIL);
		selenium.click("submitButtonIdSettings");
		
		waitForTest("User successfully modified.");
		
		verifyTrue(selenium.isTextPresent("User successfully modified."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}

	private void settingsTextSelenium() throws Exception {
		
		verifyTrue(selenium.isTextPresent("Current password:"));
		verifyTrue(selenium.isTextPresent("New password:"));
		verifyTrue(selenium.isTextPresent("Confirm new password:"));
		verifyTrue(selenium.isTextPresent("Email:"));
		verifyTrue(selenium.isTextPresent("Submit"));
		verifyTrue(selenium.isTextPresent("Cancel"));
	}
	
	
	private void waitForTest(String txt) throws InterruptedException{
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}

