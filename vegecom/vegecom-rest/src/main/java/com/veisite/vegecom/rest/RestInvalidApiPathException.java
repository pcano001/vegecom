package com.veisite.vegecom.rest;

public class RestInvalidApiPathException extends RestClientException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5962177788816988147L;

	public RestInvalidApiPathException() {
		super();
	}

	public RestInvalidApiPathException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestInvalidApiPathException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestInvalidApiPathException(String message) {
		super(message);
	}

	public RestInvalidApiPathException(Throwable cause) {
		super(cause);
	}

}
