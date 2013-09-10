package org.ourgrid.portal.test.client.common;

import java.io.File;
import java.io.IOException;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class JobSeleniumTest extends SeleneseTestCase{
	
	private DefaultSelenium selenium;
	
	private String ADMIN_LOGIN = "gustavo";
	private String ADMIN_PASSWORD = "gus";
	
	public void setUp() throws Exception {
		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
	public void testShowSubmitJobOnStartMenu() {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		verifyTrue(selenium.isTextPresent("Submit OurGrid Job"));
		
		selenium.click("submitJobItem");
		
		jobTextSelenium();
		selenium.close();
	}
	
	
	public void testSubmitJobSucess() throws InterruptedException, IOException {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitJobItem");

		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/test.zip");
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		Thread.sleep(1000);
		
		selenium.click("//input[@name='jobName']/following-sibling::img");
		selenium.mouseOver("//div[text()='echo.jdf' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='echo.jdf' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
	
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");

		Thread.sleep(1000);
		
		selenium.click("//span[text()='Job 1']");

		verifyTrue(selenium.isTextPresent("Cancel Job"));
		verifyTrue(selenium.isTextPresent("Job 1"));
		verifyTrue(selenium.isTextPresent("UNSTARTED"));
		
		verifyFalse(selenium.isTextPresent("REQUIREMENTS:"));
		
		selenium.click("//span[text()='Job 1 : EchoJob [ UNSTARTED ]']");
		
		Thread.sleep(1000);
				
		verifyTrue(selenium.isTextPresent("REQUIREMENTS:"));
		
		selenium.click("//button[text()='Cancel Job']");
		
		waitForTest("Job was cancelled successfully.");
		
		verifyTrue(selenium.isTextPresent("Job was cancelled successfully."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitJobFail() throws InterruptedException, IOException {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitJobItem");

		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/testError.zip");
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		waitForTest("JDF file doesn't found");
		
		verifyTrue(selenium.isTextPresent("JDF file doesn't found"));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	private void jobTextSelenium() {
		verifyTrue(selenium.isTextPresent("Submit"));
		verifyTrue(selenium.isTextPresent("Email notification"));
	}
	
	private void waitForTest(String txt) throws InterruptedException{
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}
