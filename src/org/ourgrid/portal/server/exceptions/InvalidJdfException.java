package org.ourgrid.portal.server.exceptions;

import org.ourgrid.portal.client.OurGridPortalException;


public class InvalidJdfException extends OurGridPortalException {

	private static final long serialVersionUID = 6447235568330138601L;

	public InvalidJdfException() {
		super();
	}

	public InvalidJdfException(String message) {
		super(message);
	}

}
