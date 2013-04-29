package com.veisite.vegecom.service.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import com.veisite.vegecom.rest.client.RestClientRequest;
import com.veisite.vegecom.rest.client.RestClientRequestFactory;
import com.veisite.vegecom.rest.client.dao.DAOExceptionHandler;
import com.veisite.vegecom.rest.client.dao.RestDAO;
import com.veisite.vegecom.service.SerializationService;

public abstract class AbstractRestDAO implements RestDAO {
	
	protected <T> T executeQuery(RestClientRequest request, HttpMethod method, 
			RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) {
		try {
			return request.execute(method, requestCallback, responseExtractor);
		} catch (RestClientException rce) {
			throw exceptionHandler.getDataAccessException(rce);
		}
	}
	
	
	@Autowired
	protected RestClientRequestFactory requestFactory;

	@Autowired
	protected SerializationService serializationService;
	
	@Autowired
	protected DAOExceptionHandler exceptionHandler;
	
	/**
	 * prefix Path to concrete DAO resources
	 */
	protected String resourcePath = "";
	

	@Override
	public String getResourcePath() {
		return resourcePath;
	}

	@Override
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	@Override
	public RestClientRequestFactory getRequestFactory() {
		return requestFactory;
	}

	@Override
	public void setRequestFactory(RestClientRequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}

	@Override
	public SerializationService getSerializationService() {
		return serializationService;
	}

	@Override
	public void setSerializationService(
			SerializationService serializationService) {
		this.serializationService = serializationService;
	}

	@Override
	public DAOExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	@Override
	public void setExceptionHandler(DAOExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

}
