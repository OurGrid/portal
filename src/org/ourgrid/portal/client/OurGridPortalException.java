package org.ourgrid.portal.client;

import java.io.Serializable;

public class OurGridPortalException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public OurGridPortalException() {
		super();
	}
	
	public OurGridPortalException(String message) {
		super(message);
	}
	
	public OurGridPortalException(Throwable cause) {
		super(cause);
	}

}
