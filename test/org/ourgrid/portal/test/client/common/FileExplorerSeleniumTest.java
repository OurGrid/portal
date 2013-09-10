package org.ourgrid.portal.test.client.common;

import java.io.File;
import java.io.IOException;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class FileExplorerSeleniumTest extends SeleneseTestCase {

	private DefaultSelenium selenium;
	
	private String ADMIN_LOGIN = "gustavo";
	private String ADMIN_PASSWORD = "gus";
	
	private String UPLOAD_FILE_NAME = "test.zip";
	private String BAD_UPLOAD_FILE_NAME = "test.blend";
	
	public void setUp() throws Exception {
		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
//	public void testShowFileExplorerMenuByShortcut() throws InterruptedException, IOException {
//		
//		selenium.start();
//		selenium.open("/OurgridPortal.html");
//		
//		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
//		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
//		selenium.click("submitButtonIdLogin");
//
//		selenium.click("fileExplorerShortcutId");
//		
//		Thread.sleep(1000);
//		
//		verifyText();
//		selenium.close();
//	}
//	
//	public void testShowFileExplorerMenuByStartMenu() throws InterruptedException, IOException {
//		
//		selenium.start();
//		selenium.open("/OurgridPortal.html");
//		
//		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
//		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
//		selenium.click("submitButtonIdLogin");
//
//		selenium.click("ux-startbutton");
//		verifyTrue(selenium.isTextPresent("File Explorer"));
//		
//		selenium.click("fileExplorerItem");
//		
//		Thread.sleep(1000);
//		
//		verifyText();
//		selenium.close();
//	}
//	
//	public void testFileUploadSuccess() throws InterruptedException, IOException {
//		
//		selenium.start();
//		selenium.open("/OurgridPortal.html");
//		
//		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
//		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
//		selenium.click("submitButtonIdLogin");
//
//		selenium.click("fileExplorerShortcutId");
//		
//		Thread.sleep(1000);
//		
//		selenium.click("//button[text()='Upload']");
//		
//		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + UPLOAD_FILE_NAME);
//		String path = file.getCanonicalPath();
//		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
//		
//		Thread.sleep(1000);
//		
//		waitForText("The file was uploaded successfully");
//		
//		verifyTrue(selenium.isTextPresent("File Uploaded"));
//		verifyTrue(selenium.isTextPresent("The file was uploaded successfully"));
//		verifyTrue(selenium.isTextPresent("Ok"));
//	
//		selenium.close();
//	}
	
	public void testFileSubmitJDFSuccess() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("fileExplorerShortcutId");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Upload']");
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + UPLOAD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		Thread.sleep(1000);
		
		waitForText("The file was uploaded successfully");
		
		verifyTrue(selenium.isTextPresent("File Uploaded"));
		verifyTrue(selenium.isTextPresent("The file was uploaded successfully"));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.click("//button[text()='Ok']");
		
		verifyTrue(selenium.isTextPresent("echo.jdf"));

		selenium.mouseOver("//span[text()='echo.jdf']");
		selenium.mouseDownRight("//div/span[text()='echo.jdf']");
		
		Thread.sleep(1000);
		
		selenium.click("//a[@id='submitJDFMenuItemIDFileExplorer']");
//		
//		waitForText("Job was added successfully.");
//		
//		verifyTrue(selenium.isTextPresent("Submit jdf succeed"));
//		verifyTrue(selenium.isTextPresent("Job was added successfully."));
//		verifyTrue(selenium.isTextPresent("Ok"));
	
		//selenium.close();
	}
	
	private void verifyText() {
		
		verifyTrue(selenium.isTextPresent("File Explorer"));
		verifyTrue(selenium.isTextPresent(ADMIN_LOGIN));
		
		verifyTrue(selenium.isTextPresent("Name"));
		verifyTrue(selenium.isTextPresent("Size"));
		verifyTrue(selenium.isTextPresent("Date"));
		
		verifyTrue(selenium.isTextPresent("Home"));
		verifyTrue(selenium.isTextPresent("Refresh"));
		verifyTrue(selenium.isTextPresent("Upload"));
	}
	
	private void waitForText(String txt) throws InterruptedException {
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}