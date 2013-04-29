package com.veisite.vegecom.rest;

public class ServerRestException extends RestException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -116245264421623338L;

	public ServerRestException() {
	}

	public ServerRestException(String message) {
		super(message);
	}

	public ServerRestException(Throwable cause) {
		super(cause);
	}

	public ServerRestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerRestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
