package org.ourgrid.portal.server.util;

import java.util.Random;

public class StringRandomizer {
	
	private static char START_CHAR = '0';
	
	private static char END_CHAR = 'z';

	private static Random RANDOM = new Random();

	public static String generateRandomAlphanumericString(int size) {
		
		if (size == 0) {
            return "";
        } else if (size < 0) {
            throw new IllegalArgumentException("Requested random string length " + size + " is less than 0.");
        }

        int gap = (END_CHAR + 1) - START_CHAR;
        
		StringBuffer buffer = new StringBuffer(size);
		
		int count = 0;
		
		while (count < size) {
			char ch = generateRandomChar(gap);
			
			if (Character.isLetterOrDigit(ch)) {
				buffer.append(ch);
				count++;
			} 
		}
		
		return buffer.toString();
	}

	private static Character generateRandomChar(int gap) {
		return (char) (RANDOM.nextInt(gap) + START_CHAR);
	}
	
}
