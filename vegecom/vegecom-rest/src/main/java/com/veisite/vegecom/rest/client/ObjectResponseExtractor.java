package com.veisite.vegecom.rest.client;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseExtractor;

import com.veisite.vegecom.service.SerializationService;


public class ObjectResponseExtractor<T> implements ResponseExtractor<T> {
	
	/**
	 * servicio de deserializacion 
	 */
	private SerializationService serializationService;
	
	/**
	 * tipo de objeto a deserializar
	 * 
	 */
	private Class<T> type;
	
	public ObjectResponseExtractor(SerializationService serializationService, Class<T> type) {
		Assert.notNull(serializationService);
		Assert.notNull(type);
		this.serializationService = serializationService;
		this.type = type;
	}
	
	
	@Override
	public T extractData(ClientHttpResponse response) throws IOException {
		return serializationService.read(response.getBody(),type);
	}

}
