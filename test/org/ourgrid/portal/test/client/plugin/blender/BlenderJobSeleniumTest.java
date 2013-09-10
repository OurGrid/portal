package org.ourgrid.portal.test.client.plugin.blender;

import java.io.File;
import java.io.IOException;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class BlenderJobSeleniumTest extends SeleneseTestCase{
	
	private DefaultSelenium selenium;
	
	private String ADMIN_LOGIN = "gustavo";
	private String ADMIN_PASSWORD = "gus";

	private String GOOD_OUTPUT_TYPE = "JPEG";
	private String GOOD_SCENES_NUMBER = "5";
	private String BAD_SCENES_NUMBER = "A";
	private String GOOD_START_FRAME_NUMBER = "1";
	private String BAD_START_FRAME_NUMBER = "A";
	private String GOOD_END_FRAME_NUMBER = "10";
	private String BAD_END_FRAME_NUMBER = "A";
	private String GOOD_FILE_NAME = "test.blend";
	private String BAD_FILE_NAME = "test.zip";

	
	public void setUp() throws Exception {
		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
	public void testShowSubmitBlenderJobOnStartMenu() {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		verifyTrue(selenium.isTextPresent("Submit Blender Job"));
		
		selenium.click("submitBlenderJobItem");
		
		blenderJobTextSelenium();
		selenium.close();
	}
	
	
	public void testSubmitBlenderJobInputFail() throws InterruptedException, IOException {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitBlenderJobItem");
		
		Thread.sleep(1000);
		
		selenium.click("//input[@name='type']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("scenesNumberIdBlenderJobs-0-input", GOOD_SCENES_NUMBER);
		selenium.type("startFrameNumberIdBlenderJobs-0-input", GOOD_START_FRAME_NUMBER);
		selenium.type("endFrameNumberIdBlenderJobs-0-input", GOOD_END_FRAME_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + BAD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		waitForTest("Please select a valid file - blend");
		
		verifyTrue(selenium.isTextPresent("Please select a valid file - blend"));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}

	public void testSubmitBlenderJobScenesNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitBlenderJobItem");
		
		Thread.sleep(1000);
		
		selenium.click("//input[@name='type']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("scenesNumberIdBlenderJobs-0-input", BAD_SCENES_NUMBER);
		selenium.type("startFrameNumberIdBlenderJobs-0-input", GOOD_START_FRAME_NUMBER);
		selenium.type("endFrameNumberIdBlenderJobs-0-input", GOOD_END_FRAME_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		waitForTest("The scenes number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The scenes number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitBlenderJobStartFrameNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitBlenderJobItem");
		
		Thread.sleep(1000);
		
		selenium.click("//input[@name='type']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("scenesNumberIdBlenderJobs-0-input", GOOD_SCENES_NUMBER);
		selenium.type("startFrameNumberIdBlenderJobs-0-input", BAD_START_FRAME_NUMBER);
		selenium.type("endFrameNumberIdBlenderJobs-0-input", GOOD_END_FRAME_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		waitForTest("The start frame number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The start frame number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitBlenderJobEndFrameNumberFail() throws InterruptedException, IOException {

		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitBlenderJobItem");
		
		Thread.sleep(1000);
		
		selenium.click("//input[@name='type']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("scenesNumberIdBlenderJobs-0-input", GOOD_SCENES_NUMBER);
		selenium.type("startFrameNumberIdBlenderJobs-0-input", GOOD_START_FRAME_NUMBER);
		selenium.type("endFrameNumberIdBlenderJobs-0-input", BAD_END_FRAME_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		waitForTest("The end frame number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The end frame number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	
	public void testSubmitJobSucess() throws InterruptedException, IOException {
	
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitBlenderJobItem");
		
		Thread.sleep(1000);
		
		selenium.click("//input[@name='type']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_OUTPUT_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("scenesNumberIdBlenderJobs-0-input", GOOD_SCENES_NUMBER);
		selenium.type("startFrameNumberIdBlenderJobs-0-input", GOOD_START_FRAME_NUMBER);
		selenium.type("endFrameNumberIdBlenderJobs-0-input", GOOD_END_FRAME_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		Thread.sleep(1000);
		
		selenium.mouseDownRight("//div[text()='" + GOOD_FILE_NAME + "']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");

		Thread.sleep(3000);

		verifyTrue(selenium.isTextPresent("Cancel Job"));
		verifyTrue(selenium.isTextPresent("Job 1"));
		verifyTrue(selenium.isTextPresent("UNSTARTED"));
		verifyTrue(selenium.isTextPresent("Tasks 1 to 2"));

		verifyFalse(selenium.isTextPresent("REQUIREMENTS:"));
		
		waitForTest("Job 1 : Blender Job [ UNSTARTED ]");
		
		selenium.click("//span[text()='Job 1 : Blender Job [ UNSTARTED ]']");

		Thread.sleep(1000);
				
		verifyTrue(selenium.isTextPresent("REQUIREMENTS:"));
		
		selenium.click("//button[text()='Cancel Job']");
		
		waitForTest("Job was cancelled successfully.");
		
		verifyTrue(selenium.isTextPresent("Job was cancelled successfully."));
		verifyTrue(selenium.isTextPresent("Ok"));
	
		selenium.close();
   }
	
	private void blenderJobTextSelenium() {
		
		verifyTrue(selenium.isTextPresent("New"));
		verifyTrue(selenium.isTextPresent("Email notification"));
		verifyTrue(selenium.isTextPresent("Output type selection:"));
		verifyTrue(selenium.isTextPresent("Scenes number:"));
		verifyTrue(selenium.isTextPresent("Start frame number:"));
		verifyTrue(selenium.isTextPresent("End frame number:"));
		verifyTrue(selenium.isTextPresent("Submit"));
	}
	
	private void waitForTest(String txt) throws InterruptedException{
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}
