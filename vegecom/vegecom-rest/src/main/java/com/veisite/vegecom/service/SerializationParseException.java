package com.veisite.vegecom.service;

/**
 * thrown where input stream cannot be parsed to deserialization.
 * 
 * @author josemaria
 *
 */
public class SerializationParseException extends SerializationException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8027921851504815207L;

	public SerializationParseException() {
	}

	public SerializationParseException(String message) {
		super(message);
	}

	public SerializationParseException(Throwable cause) {
		super(cause);
	}

	public SerializationParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
