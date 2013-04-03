package com.veisite.vegecom.rest.client;

import java.io.IOException;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;

import com.veisite.vegecom.service.SerializationService;


public class ObjectRequestFiller<T> implements RequestCallback {
	
	/**
	 * servicio de deserializacion 
	 */
	private SerializationService serializationService;
	
	/**
	 * tipo de objeto a deserializar
	 * 
	 */
	private T object;
	
	public ObjectRequestFiller(SerializationService serializationService, T object) {
		Assert.notNull(serializationService);
		Assert.notNull(object);
		this.serializationService = serializationService;
		this.object = object;
	}
	
	
	@Override
	public void doWithRequest(ClientHttpRequest request) throws IOException {
		serializationService.write(request.getBody(), object);
	}

}
