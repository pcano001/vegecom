package com.veisite.utils.dataio;

/**
 * Implementa un stream de salida de objectos genéricos que se 
 * conecta a un stream de entrada PipedObjectInputStream para
 * configurar un canal de comunicacion de un flujo de objetos
 * del mismo tipo que pasan de un lugar a otro y posiblemente
 * desde distintos threads. De hecho lo ideal es que el
 * pipedoutptstream y el pipedinputestream se ejecuten en 
 * threads distintos, en otro habria problemas.
 * 
 * @author josemaria
 *
 * @param <T>
 */
public class PipedObjectOutputFlow<T> extends ObjectOutputFlow<T> {

	/**
	 * PipedObjectInputStream al que está conectado
	 */
	PipedObjectInputFlow<T> inputPipe;
	
	/**
	 * Constructor sin argumentos
	 */
	public PipedObjectOutputFlow() {
	}
	
	/**
	 * Constructor sin argumentos
	 * @throws DataIOException 
	 */
	public PipedObjectOutputFlow(PipedObjectInputFlow<T> dest) throws DataIOException {
		connect(dest);
	}
	
	/**
	 * Conecta a un PipedObjectInputStream
	 * @param dest
	 * @throws DataIOException
	 */
	public void connect(PipedObjectInputFlow<T> dest) throws DataIOException {
		if (dest==null)
			throw new java.lang.NullPointerException("PipedObjectInputStream:connect(src): src is null");
		if (inputPipe!=null)
			throw new DataIOException("PipedObjectOutputStream is already connected");
		this.inputPipe = dest;
	}
	
	
	@Override
	public void write(T object) throws DataIOException {
		if (inputPipe==null)
			throw new DataIOException("No PipedObjectInputStream connected");
		if (isClosed()) 
			throw new DataIOException("PipedObjectOutputStream is closed");
		inputPipe.receive(object);
	}

}
