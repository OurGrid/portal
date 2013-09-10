package org.ourgrid.portal.test.client.common;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.*;

public class LoginSeleniumTest extends SeleneseTestCase {
	
	private DefaultSelenium selenium;
	
	public void setUp() throws Exception {

		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
	public void testLoginSeleniumError() throws Exception {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		loginTextSelenium();
		
		selenium.type("loginIdLogin-input", "camilla");
		selenium.type("passwordIdLogin-input", "123c");
		selenium.click("submitButtonIdLogin");
		
		waitForTest("Invalid Login or Password");
		
		verifyTrue(selenium.isTextPresent("Invalid Login or Password"));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		//selenium.close();
	}

	public void testLoginSeleniumSucess() throws Exception {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", "gustavo");
		selenium.type("passwordIdLogin-input", "gus");
		selenium.click("submitButtonIdLogin");
		
		waitForTest("");
		
		//selenium.close();
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
