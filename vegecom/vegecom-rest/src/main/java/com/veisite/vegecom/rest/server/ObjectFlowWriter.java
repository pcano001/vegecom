package com.veisite.vegecom.rest.server;

import java.io.IOException;
import java.io.OutputStream;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.OutputFlowProviderRunnable;
import com.veisite.utils.dataio.PipedObjectInputFlow;
import com.veisite.utils.dataio.PipedObjectOutputFlow;
import com.veisite.vegecom.service.SerializationService;

/**
 * Este clase conecta un flujo de objetos que proporciona un proveedor y los
 * serializa a traves del servicio de serializacion indicado. 
 * 
 * @author josemaria
 *
 * @param <T>
 */
public class ObjectFlowWriter<T> {
	
	private SerializationService serializationService;
	private OutputFlowProviderRunnable<T> provider;
	private OutputStream writer;
	
	public ObjectFlowWriter(SerializationService serializationService, 
			OutputFlowProviderRunnable<T> provider, OutputStream writer) {
		this.serializationService = serializationService;
		this.provider = provider;
		this.writer = writer;
	}
	
	public void doWrite() throws Throwable {
		PipedObjectInputFlow<T> input = null;
		PipedObjectOutputFlow<T> output = null;
		try {
			input = new PipedObjectInputFlow<T>();
			output = new PipedObjectOutputFlow<T>(input);
			// Creamos la tuberia de datos para escribir los objeto del proveedor
			input.connect(output);
			provider.setOutput(output);
			
			/* Lanzamos un thread para que el proveedor empiece a suministrar 
			 * los objetos
			 */
			Thread th = new Thread(provider);
			th.start();
			// Ahora serializamos los objetos a medida que van llegando
			serializationService.writeObjectStream(writer, input);
		} catch (DataIOException dioe) {
			throw new IOException(dioe);
		} finally {
			// We open, we close, Provider close the output
			input.close();
		}
		// Si el proveedor tuvo un error en la generacion de objetos lo reportamos.
		if (provider.getError()!=null)
			throw provider.getError();
	}

}
