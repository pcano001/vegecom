package com.veisite.vegecom.service;

/**
 * thrown when has ocurred an error serializing an object on output stream.
 * 
 * @author josemaria
 *
 */
public class SerializationGenerationException extends SerializationException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8027921851504815207L;

	public SerializationGenerationException() {
	}

	public SerializationGenerationException(String message) {
		super(message);
	}

	public SerializationGenerationException(Throwable cause) {
		super(cause);
	}

	public SerializationGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

}
