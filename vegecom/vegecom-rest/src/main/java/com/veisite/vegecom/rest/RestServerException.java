package com.veisite.vegecom.rest;

public class RestServerException extends RestException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -116245264421623338L;

	public RestServerException() {
	}

	public RestServerException(String message) {
		super(message);
	}

	public RestServerException(Throwable cause) {
		super(cause);
	}

	public RestServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestServerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
