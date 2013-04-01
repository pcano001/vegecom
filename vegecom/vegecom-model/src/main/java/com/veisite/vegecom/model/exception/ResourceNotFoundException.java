package com.veisite.vegecom.model.exception;

/**
 * Se lanza esta excepción cuando no se encuentra un objeto
 * del modelo de datos.
 * 
 * 
 * @author josemaria
 *
 */
public class ResourceNotFoundException extends DataModelException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8707482274817093176L;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
