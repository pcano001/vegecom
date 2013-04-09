package com.veisite.utils.dataio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Clase abstracta para la implementación de funcionalidad de provision de datos
 * La idea es que una clase obtenga datos de una fuente y los impulse
 * hacia unos destinos que están a la escucha. Esta clase intermedia
 * debe optimizar el acceso a la fuente.
 * Otros objetos pueden ser sumideros de estos datos a través de los listener
 * 
 *  El envio de los datos se hace en loques de un determinado tamaño. La 
 *  notificación de los bloques de informacion se hace a traves del envio de
 *  eventos.
 *  
 *  La idea es que el envio de los  bloques de informacion sea secuencial. Es
 *  decir que el flujo de los datos dede seguir un orden secuencial y no
 *  aleatorio.
 *  
 *  Esta clase abstracta recupera de forma secuencial y en bloques de determinado
 *  tamaño una lista de objetos y los envía por bloques hacia los sumideros 
 *  que están a la escucha de forma sucesiva y en orden. 
 *  
 *  Se puede utilizar como una objeto de que ayude en la carga progresiva de
 *  informacion y que se pueda ir reaccionando a la llegada de esa información
 *  por bloques.
 * 
 * @author josemaria
 *
 */
public abstract class ObjectFlowProvider<T> {
	

	private static final int MIN_CHUNKSIZE = 10; 
	private static final int MAX_CHUNKSIZE = 20000; 
	
	/**
	 * Lista de listener
	 */
	private List<ObjectFlowListener<T>> _listeners = new ArrayList<ObjectFlowListener<T>>();
	

	/**
	 * Guarda el tamaño de bloque de datos que se busca de una vez
	 * Por defecto 100
	 */
	private int blockSize=100;
	
	/**
	 * Metodo abstracto que debe recuperar una lista de objetos T desde la fuente
	 * de datos con un desplazamiento de offset y un numero como máximo de limit objetos
	 * Si la longitud de la lista es menot que limit se entiende que es el final de los
	 * datos y se tomara este bloque como el último.
	 * Si la lista es null se trata como si fuera vacia.
	 * @param blockSize
	 * 
	 * @return
	 */
	protected abstract List<T> getNextBlock(int blockSize) throws DataIOException;
	
	/**
	 * Metodo que hace una recarga completa de los datos llamando sucesivamente al
	 * metodo getChunk para obtener bloques de datos y lanza los eventos adecuados
	 * a los destinos que están a la escucha.
	 * @throws DataIOException 
	 */
	public void fireStartObjectFlow() throws DataIOException {
		fireObjectFlowInit();
		List<T> lista=null;
		do {
			try {
				lista = getNextBlock(blockSize);
				fireObjectFlowBlock(lista);
			} catch (DataIOException dioe) {
				if (dioe.getCause()!=null) fireObjectFlowError(dioe.getCause());
				else fireObjectFlowError(dioe);
				return;
			}
		} while (lista!=null && lista.size()>=blockSize);
		fireObjectFlowEnd();
	}
	
	
	/**
	 * @return the chunkSize
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @param blockSize the chunkSize to set
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = Math.min(MAX_CHUNKSIZE, Math.max(MIN_CHUNKSIZE, blockSize));
	}
	
	
	public synchronized void addObjectFlowListener(ObjectFlowListener<T> listener) {
		_listeners.add(listener);
	}

	public synchronized void removeObjectFlowListener(ObjectFlowListener<T> listener) {
		_listeners.remove(listener);
	}

	/**
	 * Notificar el inicio del flujo
	 */
	private synchronized void fireObjectFlowInit() {
		Iterator<ObjectFlowListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((ObjectFlowListener<T>) i.next()).objectFlowInit();
		}
	}

	/**
	 * Notificar la llegada de un bloque de informacion
	 */
	private synchronized void fireObjectFlowBlock(List<T> block) {
		Iterator<ObjectFlowListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((ObjectFlowListener<T>) i.next()).objectFlowBlock(block);
		}
	}

	/**
	 * Notificar el fin del flujo
	 */
	private synchronized void fireObjectFlowEnd() {
		Iterator<ObjectFlowListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((ObjectFlowListener<T>) i.next()).objectFlowEnd();
		}
	}

	protected void fireObjectFlowError(Throwable t) {
		Iterator<ObjectFlowListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((ObjectFlowListener<T>) i.next()).objectFlowError(t);
		}
	}
		

}
