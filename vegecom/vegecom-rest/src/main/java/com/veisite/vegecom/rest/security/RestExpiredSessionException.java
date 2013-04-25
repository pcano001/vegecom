package com.veisite.vegecom.rest.security;


public class RestExpiredSessionException extends RestSecurityException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5674309672263809910L;

	public RestExpiredSessionException() {
	}

	public RestExpiredSessionException(String message) {
		super(message);
	}

	public RestExpiredSessionException(Throwable cause) {
		super(cause);
	}

	public RestExpiredSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestExpiredSessionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
