package com.veisite.utils.dataio;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Proveedor de datos que hace uso de un canal de comunicacion de 
 * formado por un piped object input stream y un piped object 
 * output stream y lanza los eventos de provision de datos a los
 * listener.
 * Las subclases deben proporcionar un escritor de objetos en el
 * output stream. Todo los demas los gestiona esta clase.
 * 
 * de datos. 
 * 
 * @author josemaria
 *
 * @param <T>
 */
public abstract class AbstractPipedListProvider<T> extends ObjectFlowProvider<T> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractPipedListProvider.class);
	
	/**
	 * input stream
	 */
	private PipedObjectInputFlow<T> input;
	
	/**
	 * output stream
	 */
	private PipedObjectOutputFlow<T> output;
	
	/**
	 * Error capturado en la escritura
	 */
	private Throwable outputError;
	
	/**
	 * La subclases deben implementar este metodo para escribir en el
	 * output stream la lista de objetos.
	 * 
	 */
	protected abstract void writeOutputStream(ObjectOutputFlow<T> output) 
			throws Throwable;
	
	/**
	 * recoge los objetos del input stream
	 * 
	 */
	@Override
	protected List<T> getNextBlock(int maxSize) throws DataIOException {
		List<T> lista = new ArrayList<T>();
		while (maxSize>0) {
			T e = input.read();
			if (e==null) {
				// Fin de flujo, si hubo error en escritura notificar
				Throwable t = getOutputError();
				if (t!=null) 
					throw new DataIOException("Error en escritura",t);
				logger.debug("Detectado fin de flujo de entrada, cerrando...");
				input.close();
				return lista;
			}
			lista.add(e);
			maxSize--;
		}
		return lista;
	}

	
	/**
	 * @return the input
	 */
	public PipedObjectInputFlow<T> getInput() {
		return input;
	}

	/**
	 * @return the output
	 */
	public PipedObjectOutputFlow<T> getOutput() {
		return output;
	}


	/**
	 * @return the outputError
	 */
	public Throwable getOutputError() {
		return outputError;
	}

	/**
	 * @param outputError the outputError to set
	 */
	public void setOutputError(Throwable outputError) {
		this.outputError = outputError;
	}

	/**
	 * Se lanza una carga.
	 * Creamos una conexin de tuberias y lanzamos los escritores
	 * y lectores
	 * @throws DataIOException 
	 */
	public void fireStartObjectFlow() throws DataIOException {
		input = new PipedObjectInputFlow<T>();
		output = new PipedObjectOutputFlow<T>(input);
		input.connect(output);
		setOutputError(null);
		/* Lanzamos un thread para el escritor del buffer */
		Thread th = new Thread(new Runnable() {
			public void run() {
				try {
					writeOutputStream(output);
				} catch (Throwable t) {
					setOutputError(t);
				} finally {
					// We open, we close
					output.close();
				}
			}
		});
		/* Lanza la escritura de objetos */
		th.start();
		/* Ahora la lectura de objetos */
		super.fireStartObjectFlow();
	}

}
