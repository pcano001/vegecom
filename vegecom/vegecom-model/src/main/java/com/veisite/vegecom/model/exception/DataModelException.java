package com.veisite.vegecom.model.exception;

/**
 * Se intenta crear un objeto nuevo y se indica un identificador
 * lo que no está permitido.
 * 
 * @author josemaria
 *
 */
public class DataModelException extends VegecomException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8707482274817093176L;

	public DataModelException() {
	}

	public DataModelException(String message) {
		super(message);
	}

	public DataModelException(Throwable cause) {
		super(cause);
	}

	public DataModelException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataModelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
