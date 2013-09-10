package org.ourgrid.portal.test.client.plugin.genecodis;

import java.io.File;
import java.io.IOException;

import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class GenecodisJobSeleniumTest extends SeleneseTestCase {

	private DefaultSelenium selenium;
	
	private String ADMIN_LOGIN = "gustavo";
	private String ADMIN_PASSWORD = "gus";

	private String GOOD_SUPPORT_NUMBER = "3";
	private String BAD_SUPPORT_NUMBER = "A";
	private String GOOD_SUPPORT_FOR_RANDOM_NUMBER = "3";
	private String BAD_SUPPORT_FOR_RANDOM_NUMBER = "A";
	
	private String GOOD_ANALYSIS_TYPE = "CONCURRENCE ANALYSIS";
	
	private String GOOD_REFERENCE_SIZE_NUMBER = "31804";
	private String BAD_REFERENCE_SIZE_NUMBER = "A";
	private String GOOD_SELECTED_REFERENCE_SIZE_NUMBER = "735";
	private String BAD_SELECTED_REFERENCE_SIXE_NUMBER = "A";
	
	private String GOOD_STATISTICAL_TEST_TYPE = "BOTH";
	
	private String GOOD_P_VALUE_NUMBER = "1";
	private String BAD_P_VALUE_NUMBER = "A";
	
	private String GOOD_FILE_NAME = "test.engene";
	private String BAD_FILE_NAME = "test.zip";

	public void setUp() throws Exception {
		ConnectSelenium conn = new ConnectSelenium();
		selenium = conn.createSeleniumClient("http://127.0.0.1:8888/");
	}
	
	public void testShowSubmitBlenderJobOnStartMenu() throws InterruptedException, IOException {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		verifyTrue(selenium.isTextPresent("Submit Genecodis Job"));
		
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		verifyText();
		selenium.close();
	}
	
	public void testSubmitGenecodisJobInputFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + BAD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		waitForTest("Please select a valid file - engene");
		
		verifyTrue(selenium.isTextPresent("Please select a valid file - engene"));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobSupportNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", BAD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");
		
		waitForTest("The support number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The support number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobSupportForRandomNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", BAD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");
		
		waitForTest("The support for random number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The support for random number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobReferenceSizeNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", BAD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");
		
		waitForTest("The reference size number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The reference size number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobSelectedReferenceSizeNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", BAD_SELECTED_REFERENCE_SIXE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");
		
		waitForTest("The selected reference size number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The selected reference size number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobPValueNumberFail() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", BAD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");
		
		waitForTest("The P-Value number is not valid.");
		
		verifyTrue(selenium.isTextPresent("The P-Value number is not valid."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobNoFileSelectedFail() throws InterruptedException, IOException {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.click("//button[text()='Submit']");
		
		waitForTest("No file selected.");
		
		verifyTrue(selenium.isTextPresent("No file selected."));
		verifyTrue(selenium.isTextPresent("Ok"));

		selenium.close();
	}
	
	public void testSubmitGenecodisJobSuccess() throws InterruptedException, IOException {
		
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("submitGenecodisJobItem");
		
		Thread.sleep(1000);
		
		selenium.type("supportNumberFieldIdJobs-0-input", GOOD_SUPPORT_NUMBER);
		selenium.type("supportForRandomIdJobs-0-input", GOOD_SUPPORT_FOR_RANDOM_NUMBER);
		
		selenium.click("//input[@name='analysisType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_ANALYSIS_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("referenceSizeIdJobs-0-input", GOOD_REFERENCE_SIZE_NUMBER);
		selenium.type("selectedRefSizeIdJobs-0-input", GOOD_SELECTED_REFERENCE_SIZE_NUMBER);
		
		selenium.click("//input[@name='statisticalTestType']/following-sibling::img");
		selenium.mouseOver("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		selenium.mouseDownRight("//div[text()='" + GOOD_STATISTICAL_TEST_TYPE + "' and not(ancestor::*[contains(@style,'display: none') or contains(@style, 'visibility: hidden') or contains(@class,'x-hide-display')])]");
		
		Thread.sleep(1000);
		
		selenium.type("pValueIdJobs-0-input", GOOD_P_VALUE_NUMBER);
		
		Thread.sleep(1000);
		
		File file = new File ("./src/org/ourgrid/portal/test/client/testsFiles/" + GOOD_FILE_NAME);
		String path = file.getCanonicalPath();
		selenium.type("//input[@type='file']", path.replaceAll("/", "\\"));
		
		selenium.click("//button[text()='Send']");
		
		Thread.sleep(1000);
		
		selenium.mouseDownRight("//div[text()='" + GOOD_P_VALUE_NUMBER + "']");
		selenium.click("//button[text()='Submit']");
		
		Thread.sleep(3000);

		verifyTrue(selenium.isTextPresent("Cancel Job"));
		verifyTrue(selenium.isTextPresent("Job 1"));
		verifyTrue(selenium.isTextPresent("UNSTARTED"));
		verifyTrue(selenium.isTextPresent("Tasks 1 to 1"));

		verifyFalse(selenium.isTextPresent("REQUIREMENTS:"));
		
		waitForTest("Job 1 : GeneCodis Job [ UNSTARTED ]");
		
		selenium.click("//span[text()='Job 1 : GeneCodis Job [ UNSTARTED ]']");

		Thread.sleep(1000);
				
		verifyTrue(selenium.isTextPresent("REQUIREMENTS:"));
		
		selenium.click("//button[text()='Cancel Job']");
		
		waitForTest("Job was cancelled successfully.");
		
		verifyTrue(selenium.isTextPresent("Job was cancelled successfully."));
		verifyTrue(selenium.isTextPresent("Ok"));
	
		selenium.close();
	}
	
	private void verifyText() {
		
		verifyTrue(selenium.isTextPresent("New"));
		
		verifyTrue(selenium.isTextPresent("Generate all possibilities"));
		verifyTrue(selenium.isTextPresent("Support"));
		verifyTrue(selenium.isTextPresent("Support for random number"));
		verifyTrue(selenium.isTextPresent("Analysis type selection"));
		verifyTrue(selenium.isTextPresent("Reference size"));
		verifyTrue(selenium.isTextPresent("Selected reference size"));
		verifyTrue(selenium.isTextPresent("Statistical test type selection"));
		verifyTrue(selenium.isTextPresent("P-Value"));
		
		verifyTrue(selenium.isTextPresent("Email notification"));
		verifyTrue(selenium.isTextPresent("Send"));
		
		verifyTrue(selenium.isTextPresent("Submission Status"));
		verifyTrue(selenium.isTextPresent("Submit"));
	}
	
	private void waitForTest(String txt) throws InterruptedException {
		
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(txt)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
	}
}