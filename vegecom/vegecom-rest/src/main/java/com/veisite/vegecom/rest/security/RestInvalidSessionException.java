package com.veisite.vegecom.rest.security;


public class RestInvalidSessionException extends RestSecurityException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5674309672263809910L;

	public RestInvalidSessionException() {
	}

	public RestInvalidSessionException(String message) {
		super(message);
	}

	public RestInvalidSessionException(Throwable cause) {
		super(cause);
	}

	public RestInvalidSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestInvalidSessionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
