package org.ourgrid.portal.server.logic.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;

//TODO Camilla - Colocar isso como EncryptTO
public class Encryptor {

	private static String MD5_ENCRYPT_TYPE = "MD5";
	
	public static String go (String password) throws OurGridPortalClientException {  
	     MessageDigest md;
		try {
			md = MessageDigest.getInstance(MD5_ENCRYPT_TYPE);
			BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));  
			String passw = hash.toString(16);  
			if (passw.length() %2 != 0) {
		         passw = String.valueOf(passw); 
		    }
		    return passw;
		} catch (NoSuchAlgorithmException e) {
			throw new OurGridPortalClientException("Problems on server. Try again later.");
		}  
	}
}
