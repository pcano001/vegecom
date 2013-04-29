package com.veisite.vegecom.rest.client.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.rest.client.RestClientRequest;
import com.veisite.vegecom.rest.client.RestClientRequestFactory;
import com.veisite.vegecom.rest.security.RestApiKeySession;
import com.veisite.vegecom.rest.security.RestSecurity;
import com.veisite.vegecom.service.SerializationService;

public class DefaultRestSessionDAO implements RestSessionDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	protected RestClientRequestFactory requestFactory;

	protected SerializationService serializationService;
	
	protected DAOExceptionHandler exceptionHandler;
	
	/**
	 * prefix Path to concrete DAO resources
	 */
	protected String resourcePath = "";

	@Override
	public RestApiKeySession createSession(String user, String password) {
		RestClientRequest request = requestFactory.createRequest(getResourcePath());
		// Tomemos el timestamp del cliente
		Long ms = new Date().getTime();
		Long sTimestamp = Math.round(((double)ms) / 1000.0d);
		request.setParameter(RestSecurity.USER_PARAMETER, user);
		request.setParameter(RestSecurity.PASSWORD_PARAMETER, password);
		request.setParameter(RestSecurity.TIMESTAMP_PARAMETER, Long.toString(sTimestamp));
		// Make a secure request
		request.setScheme("https");
		
		RequestCallback cb = null; 
		ResponseExtractor<RestApiKeySession> ex = 
				new ObjectResponseExtractor<RestApiKeySession>(serializationService, RestApiKeySession.class);
		try {
			logger.debug("Requesting a new session for user {}.",user);
			return request.execute(HttpMethod.POST, cb, ex);
		} catch (RestClientException rce) {
			throw exceptionHandler.getDataAccessException(rce);
		}
	}

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
