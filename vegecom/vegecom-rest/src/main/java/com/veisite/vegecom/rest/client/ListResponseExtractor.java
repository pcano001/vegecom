package com.veisite.vegecom.rest.client;

import java.io.IOException;
import java.util.List;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseExtractor;

import com.veisite.vegecom.service.SerializationService;


public class ListResponseExtractor<T> implements ResponseExtractor<List<T>> {
	
	/**
	 * servicio de deserializacion 
	 */
	private SerializationService serializationService;
	
	/**
	 * tipo de objeto de la lista a deserializar
	 * 
	 */
	private Class<T> type;
	
	public ListResponseExtractor(SerializationService serializationService, Class<T> type) {
		Assert.notNull(serializationService);
		Assert.notNull(type);
		this.serializationService = serializationService;
		this.type = type;
	}
	
	
	@Override
	public List<T> extractData(ClientHttpResponse response) throws IOException {
		return serializationService.readList(response.getBody(),type);
	}

}
