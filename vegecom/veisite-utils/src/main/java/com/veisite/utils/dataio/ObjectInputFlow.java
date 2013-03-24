package com.veisite.utils.dataio;

public abstract class ObjectInputFlow<T> {
	
	/**
	 * Indica si el flujo está cerrado.
	 */
	private boolean closed = false;

	/**
	 * Intenta leer un objeto en el flujo de entrada.
	 * Devuelve null si no es posible hacerlo y el objeto si se hace
	 * 
	 * Si el flujo esta cerrado (closed==true) devuelve null.
	 * 
	 * Devolver null intenta decir que no hay más lecturas posible por
	 * lo que las implementaciones deberian hacerlo sólo en el caso de 
	 * estar seguras de que no hay más que leer, en otro caso deberían 
	 * bloquear hasta que haya datos.
	 * 
	 * @param object
	 * @return
	 */
	public abstract T read() throws DataIOException;

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
