package com.veisite.vegecom.rest;

public class RestClientException extends RestException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -116245264421623338L;

	public RestClientException() {
	}

	public RestClientException(String message) {
		super(message);
	}

	public RestClientException(Throwable cause) {
		super(cause);
	}

	public RestClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestClientException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
