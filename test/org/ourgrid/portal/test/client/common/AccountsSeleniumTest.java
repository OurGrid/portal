package org.ourgrid.portal.test.client.common;

import java.io.FileWriter;
import java.io.IOException;

import org.ourgrid.portal.server.logic.executors.common.CaptchaGeneratorExecutor;
import org.ourgrid.portal.server.util.CaptchaUtil;
import org.ourgrid.portal.test.client.ConnectSelenium;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

public class AccountsSeleniumTest extends SeleneseTestCase{

	private DefaultSelenium selenium;
	
	private String ADMIN_LOGIN = "gustavo";
	private String ADMIN_PASSWORD = "gus";
	
	private String USER_LOGIN = "testAccounts";
	private String USER_PASSWORD = "testAccounts";
	private String USER_EMAIL = "testAccounts@gmail.com";
	
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
	
	
	public void testShowAccountsFieldOnStartMenu() throws Exception {
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		
		waitForTest("Accounts");
		
		verifyTrue(selenium.isTextPresent("Accounts"));
		
		selenium.click("accountsItem");
		
		Thread.sleep(1000);
		
		accountsTextSelenium();
		
		selenium.close();
	}
	
	public void testAccountsSeleniumApproveUserSucess() throws Exception {

		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		registerUser();
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("accountsItem");
		
		accountsTextSelenium();
		
		selenium.mouseDownRight("//div[text()='" + USER_LOGIN + "']");
		selenium.click("approveUserButtonIdAccounts");
		
		waitForTest("Users sucessfully approved.");
		
		verifyTrue(selenium.isTextPresent("Users sucessfully approved."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.click("//button[text()='Ok']");
		
		selenium.close();
		configureBuildTest("false");
	}
	
	public void testNotShowAccountsFieldOnStartMenu() throws Exception {
		
		configureBuildTest("true");
		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", USER_LOGIN);
		selenium.type("passwordIdLogin-input", USER_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		
		Thread.sleep(1000);
		
		Boolean isAccountsPresent = selenium.isTextPresent("Accounts");
		
		if(!isAccountsPresent){
			verifyTrue(true);
		}
		
		selenium.close();
		configureBuildTest("false");
	}
	
	public void testAccountsSeleniumFailApproveUser() throws Exception {

		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("accountsItem");
		
		selenium.mouseDownRight("//div[text()='" + USER_LOGIN + "']");
		selenium.click("approveUserButtonIdAccounts");
		
		waitForTest(USER_LOGIN + " is already approved.");
		
		verifyTrue(selenium.isTextPresent(USER_LOGIN + " is already approved."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}
	
	public void testAccountsSeleniumRemoveUserSucess() throws Exception {

		selenium.start();
		selenium.open("/OurgridPortal.html");
		
		selenium.type("loginIdLogin-input", ADMIN_LOGIN);
		selenium.type("passwordIdLogin-input", ADMIN_PASSWORD);
		selenium.click("submitButtonIdLogin");

		selenium.click("ux-startbutton");
		selenium.click("accountsItem");
		
		selenium.mouseDownRight("//div[text()='" + USER_LOGIN + "']");
		selenium.click("removeUserButtonIdAccounts");
		
		waitForTest("Users sucessfully removed.");
		
		verifyTrue(selenium.isTextPresent("Users sucessfully removed."));
		verifyTrue(selenium.isTextPresent("Ok"));
		
		selenium.close();
	}
	
	private void accountsTextSelenium() {
		verifyTrue(selenium.isTextPresent("Login"));
		verifyTrue(selenium.isTextPresent("E-mail"));
		verifyTrue(selenium.isTextPresent("Approve"));
		verifyTrue(selenium.isTextPresent("Remove"));
		verifyTrue(selenium.isTextPresent("Close"));
	}
	
	private void registerUser() throws InterruptedException {
		
		selenium.click("registerButtonIdLogin");
		selenium.type("loginIdRegister-input", USER_LOGIN);
		selenium.type("passwordIdRegister-input", USER_PASSWORD);
		selenium.type("confirmPasswordIdRegister-input", USER_PASSWORD);
		selenium.type("emailIdRegister-input", USER_EMAIL);
		selenium.type("captchaIdRegister-input", GOOD_CAPTCHA);
		selenium.click("submitButtonIdRegister");
		Thread.sleep(10000);
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
