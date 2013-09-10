package org.ourgrid.portal.server.util;

public abstract class CaptchaUtil {
	
	private static boolean isBuildTest = false;

	public static boolean isBuildTest() {
		return isBuildTest;
	}

	public static void setBuildTest(boolean isBuidTest) {
		isBuildTest = isBuidTest;
	}
}
