package com.veisite.vegecom.model.exception;

public class VegecomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VegecomException() {
		super();
	}

	public VegecomException(String message, Throwable cause) {
		super(message, cause);
	}

	public VegecomException(String message) {
		super(message);
	}

	public VegecomException(Throwable cause) {
		super(cause);
	}

	public VegecomException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
