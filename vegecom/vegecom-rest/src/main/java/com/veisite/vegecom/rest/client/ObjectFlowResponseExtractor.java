package com.veisite.vegecom.rest.client;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseExtractor;

import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.service.SerializationService;


public class ObjectFlowResponseExtractor<T> implements ResponseExtractor<T> {
	
	/**
	 * servicio de deserializacion 
	 */
	private SerializationService serializationService;
	
	/**
	 * tipo de objeto a deserializar
	 * 
	 */
	private Class<T> type;
	
	/**
	 * Flujo de lectura al que se mandaran los objetos leidos.
	 */
	private ObjectOutputFlow<T> output;
	
	/**
	 * 
	 * @param serializationService
	 * @param type
	 */
	public ObjectFlowResponseExtractor(SerializationService serializationService, 
			ObjectOutputFlow<T> output, Class<T> type) {
		Assert.notNull(serializationService);
		Assert.notNull(output);
		Assert.notNull(type);
		this.serializationService = serializationService;
		this.output = output;
		this.type = type;
	}
	
	
	@Override
	public T extractData(ClientHttpResponse response) throws IOException {
		serializationService.readObjectStream(response.getBody(), output, type);
		return null;
	}

}
