package com.veisite.vegecom.service;

/**
 * thrown when cannot obtain or generate an object from a
 * deserialization or to seralization
 * 
 * @author josemaria
 *
 */
public class UncategorizedSerializationException extends SerializationException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8027921851504815207L;

	public UncategorizedSerializationException() {
	}

	public UncategorizedSerializationException(String message) {
		super(message);
	}

	public UncategorizedSerializationException(Throwable cause) {
		super(cause);
	}

	public UncategorizedSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
