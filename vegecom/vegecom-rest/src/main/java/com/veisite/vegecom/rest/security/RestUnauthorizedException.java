package com.veisite.vegecom.rest.security;


public class RestUnauthorizedException extends RestSecurityException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5674309672263809910L;

	public RestUnauthorizedException() {
	}

	public RestUnauthorizedException(String message) {
		super(message);
	}

	public RestUnauthorizedException(Throwable cause) {
		super(cause);
	}

	public RestUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestUnauthorizedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
