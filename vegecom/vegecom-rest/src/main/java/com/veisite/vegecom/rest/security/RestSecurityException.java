package com.veisite.vegecom.rest.security;

import com.veisite.vegecom.rest.ClientRestException;

public abstract class RestSecurityException extends ClientRestException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5962177788816988147L;

	public RestSecurityException() {
		super();
	}

	public RestSecurityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestSecurityException(String message) {
		super(message);
	}

	public RestSecurityException(Throwable cause) {
		super(cause);
	}

	
}
