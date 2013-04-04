package com.veisite.vegecom.service;

/**
 * thrown when cannot obtain or generate an object from a
 * deserialization or to seralization
 * 
 * @author josemaria
 *
 */
public class SerializationMappingException extends SerializationException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8027921851504815207L;

	public SerializationMappingException() {
	}

	public SerializationMappingException(String message) {
		super(message);
	}

	public SerializationMappingException(Throwable cause) {
		super(cause);
	}

	public SerializationMappingException(String message, Throwable cause) {
		super(message, cause);
	}

}
