package com.veisite.vegecom.model.exception;

/**
 * Se lanza esta excepción cuando se espera un objeto del modelo
 * como parametro y este no es válido (nulo o incorrecto).
 * 
 * 
 * @author josemaria
 *
 */
public class InvalidResourceParameterException extends DataModelException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8707482274817093176L;

	public InvalidResourceParameterException() {
	}

	public InvalidResourceParameterException(String message) {
		super(message);
	}

	public InvalidResourceParameterException(Throwable cause) {
		super(cause);
	}

	public InvalidResourceParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidResourceParameterException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
