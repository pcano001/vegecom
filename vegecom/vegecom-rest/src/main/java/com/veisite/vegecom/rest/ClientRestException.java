package com.veisite.vegecom.rest;

public class ClientRestException extends RestException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -116245264421623338L;

	public ClientRestException() {
	}

	public ClientRestException(String message) {
		super(message);
	}

	public ClientRestException(Throwable cause) {
		super(cause);
	}

	public ClientRestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientRestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
