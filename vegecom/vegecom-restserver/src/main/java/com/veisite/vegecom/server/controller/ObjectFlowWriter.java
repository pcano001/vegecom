package com.veisite.vegecom.server.controller;

import java.io.IOException;
import java.io.OutputStream;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.OutputFlowProviderRunnable;
import com.veisite.utils.dataio.PipedObjectInputFlow;
import com.veisite.utils.dataio.PipedObjectOutputFlow;
import com.veisite.vegecom.rest.SerializationService;

public class ObjectFlowWriter<T> {
	
	private SerializationService serializationService;
	private OutputFlowProviderRunnable<T> provider;
	private OutputStream writer;
	
	public ObjectFlowWriter(SerializationService jsonService, OutputFlowProviderRunnable<T> provider, OutputStream writer) {
		this.serializationService = jsonService;
		this.provider = provider;
		this.writer = writer;
	}
	
	public void doWrite() throws IOException {
		try {
			// Creamos la tuberias de datos para imprimir
			final PipedObjectInputFlow<T> input = new PipedObjectInputFlow<T>();
			final PipedObjectOutputFlow<T> output = new PipedObjectOutputFlow<T>(input);
			input.connect(output);
			provider.setOutput(output);
			
			/* Lanzamos un thread para la lectura de datos en base de datos */
			Thread th = new Thread(provider);
			/* Lanza la lectura de la base de datos */
			th.start();
			// Lanza la escritura Json
			serializationService.writeObjectStream(writer, input);
		} catch (DataIOException dioe) {
			throw new IOException(dioe);
		}
		if (provider.getError()!=null)
			throw new IOException(provider.getError());
	}

}
