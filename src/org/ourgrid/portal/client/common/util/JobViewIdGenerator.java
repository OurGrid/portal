package org.ourgrid.portal.client.common.util;

public class JobViewIdGenerator {

	private static int value = 0;
	
	public static int getNextJobViewId() {
		return value++;
	}

}
