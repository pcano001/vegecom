package com.veisite.vegecom.rest.security;


public class RestUnauthenticatedException extends RestSecurityException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5674309672263809910L;

	public RestUnauthenticatedException() {
	}

	public RestUnauthenticatedException(String message) {
		super(message);
	}

	public RestUnauthenticatedException(Throwable cause) {
		super(cause);
	}

	public RestUnauthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestUnauthenticatedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
