package com.veisite.vegecom.rest.client.dao;

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * Excepcion lanzada cuando se recibe un error 5xx del servidor y no 
 * se tiene mas informacion (no hay objeto RestError en el cuerpo)
 * 
 * @author josemaria
 *
 */
public class HelperRestErrorException extends
		UncategorizedDataAccessException {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 253900328220253908L;

	public HelperRestErrorException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
