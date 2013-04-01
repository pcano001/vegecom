package com.veisite.vegecom.model.exception;

/**
 * Se intenta crear un objeto nuevo y se indica un identificador
 * lo que no está permitido.
 * 
 * @author josemaria
 *
 */
public class NewResourceWithExplicitIdException extends DataModelException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8707482274817093176L;

	public NewResourceWithExplicitIdException() {
	}

	public NewResourceWithExplicitIdException(String message) {
		super(message);
	}

	public NewResourceWithExplicitIdException(Throwable cause) {
		super(cause);
	}

	public NewResourceWithExplicitIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public NewResourceWithExplicitIdException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
