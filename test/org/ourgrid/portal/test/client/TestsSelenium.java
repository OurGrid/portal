package org.ourgrid.portal.test.client;
import org.ourgrid.portal.test.client.common.AccountsSeleniumTest;
import org.ourgrid.portal.test.client.common.JobSeleniumTest;
import org.ourgrid.portal.test.client.common.LoginSeleniumTest;
import org.ourgrid.portal.test.client.common.RecoverPasswordSeleniumTest;
import org.ourgrid.portal.test.client.common.RegisterSeleniumTest;
import org.ourgrid.portal.test.client.common.SettingsSeleniumTest;
import org.ourgrid.portal.test.client.plugin.blender.BlenderJobSeleniumTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestsSelenium {

	public static Test suite() {

		TestSuite suite = new TestSuite("OurGrid Portal Tests");

		suite.addTestSuite(RegisterSeleniumTest.class);
		suite.addTestSuite(RecoverPasswordSeleniumTest.class);
		suite.addTestSuite(LoginSeleniumTest.class);
		suite.addTestSuite(AccountsSeleniumTest.class);
		suite.addTestSuite(SettingsSeleniumTest.class);
		suite.addTestSuite(JobSeleniumTest.class);
		suite.addTestSuite(BlenderJobSeleniumTest.class);
		
		return suite;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			junit.textui.TestRunner.run(TestsSelenium.suite());
		} 
	}

}
