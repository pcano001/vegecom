package com.veisite.utils.dataio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Clase conectar a un PipedObjectOutputStream y formar una canal
 * de comunicacion y flujo de objetos del mismo tipo.
 * 
 *  Dispone de un buffer de almacenamiento para regular el flujo
 *  entre la entrada y la salida.
 * 
 *  Se implementa un timeout de forma que si no es posible
 *  escribir el el flujo de salida en ese tiempo se lanza una
 *  excepcion
 * 
 * @author josemaria
 *
 * @param <T>
 */
public class PipedObjectInputFlow<T> extends ObjectInputFlow<T> {

	/**
	 * Capacidad por defecto
	 */
	private static final int BUFFER_SIZE = 512;
	
	/**
	 * Timeout por defecto para la esritura y lectura del 
	 * buffer.
	 */
	private int timeout = 50;
	
	/**
	 * Reintentos en la escritura y lectura del buffer.
	 * En total cinco segundos. (retries * timeout)
	 */
	private int retries = 100;
	
	/**
	 * Buffer de intercambio
	 * Por defecto con una capacidad de 512
	 */
	private ArrayBlockingQueue<T> buffer;
	
	/**
	 * Tuberia de salida a la que esta conectada
	 */
	private PipedObjectOutputFlow<T> outputPipe;

	
	public PipedObjectInputFlow() {
		buffer = new ArrayBlockingQueue<T>(BUFFER_SIZE);
	}
	
	public PipedObjectInputFlow(int capacity) {
		buffer = new ArrayBlockingQueue<T>(capacity);
	}
	
	public PipedObjectInputFlow(PipedObjectOutputFlow<T> src)
		throws DataIOException {
		buffer = new ArrayBlockingQueue<T>(BUFFER_SIZE);
		connect(src);
	}
	
	public PipedObjectInputFlow(PipedObjectOutputFlow<T> src, int capacity) 
		throws DataIOException {
		buffer = new ArrayBlockingQueue<T>(capacity);
		connect(src);
	}
	
	public void connect(PipedObjectOutputFlow<T> src) throws DataIOException {
		if (src==null)
			throw new java.lang.NullPointerException("PipedObjectInputStream:connect(src): src is null");
		if (outputPipe!=null)
			throw new DataIOException("PipedObjectInputStream is already connected");
		this.outputPipe = src;
	}
	
	@Override
	public T read() throws DataIOException {
		if (outputPipe==null)
			throw new DataIOException("No PipedObjectOutputStream connected");
		if (isClosed()) 
			throw new DataIOException("PipedObjectInputStream is closed");
		int r = retries;
		while (r>0) {
			T e;
			try {
				e = buffer.poll(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException ie) {
				throw new DataIOException("read timeout was interrumpted");
			}
			if (e!=null) return e;
			if (outputPipe.isClosed()) return null;
			r--;
		}
		return null;
	}
	
	/**
	 * Recibe un objeto para el buffer que queda disponible para
	 * su lectura. Si el objeto no pudiera dejarse la llamada se
	 * bloquea en espera de que haya espacio en el buffer o se 
	 * interrumpa la espera por el tiempo indicado en timeout.
	 * 
	 * @param item
	 * @return
	 */
	public void receive(T object) throws DataIOException {
		if (outputPipe==null)
			throw new DataIOException("No PipedObjectOutputStream connected");
		if (isClosed()) 
			throw new DataIOException("PipedObjectInputStream is closed");
		if (buffer.offer(object)) return;
		try {
			if (buffer.offer(object, timeout, TimeUnit.MILLISECONDS))
				return;
		} catch (InterruptedException ie) {	}
		throw new DataIOException("Cannot write to buffer in PipedObjectInputStream");
	}

}
