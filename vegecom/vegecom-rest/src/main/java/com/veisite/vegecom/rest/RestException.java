package com.veisite.vegecom.rest;

public class RestException extends RuntimeException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -116245264421623338L;

	public RestException() {
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(Throwable cause) {
		super(cause);
	}

	public RestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
