package com.veisite.utils.dataio;

public abstract class ObjectOutputFlow<T> {
	
	/**
	 * Indica si el flujo está cerrado.
	 */
	private boolean closed = false;

	/**
	 * Escribe un objeto en el flujo de salida.
	 *
	 * Si el flujo esta cerrado o se produce algun error
	 * se lanza una excepcion.
	 * 
	 * @param object
	 * @return
	 */
	public abstract void write(T object) throws DataIOException;

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void close() {
		this.closed = true;
	}

}
