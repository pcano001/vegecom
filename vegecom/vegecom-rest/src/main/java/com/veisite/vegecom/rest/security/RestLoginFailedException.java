package com.veisite.vegecom.rest.security;

/**
 * Exception thrown when login failed to realm and cannot be supply a valid session key
 * 
 * @author josemaria
 *
 */
public class RestLoginFailedException extends RestSecurityException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5674309672263809910L;

	public RestLoginFailedException() {
	}

	public RestLoginFailedException(String message) {
		super(message);
	}

	public RestLoginFailedException(Throwable cause) {
		super(cause);
	}

	public RestLoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestLoginFailedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
