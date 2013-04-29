package com.veisite.vegecom.rest.client.dao;

import com.veisite.vegecom.rest.client.RestClientRequestFactory;
import com.veisite.vegecom.service.SerializationService;

public interface RestDAO {
	
	/**
	 * @return the servicePath
	 */
	public String getResourcePath();

	/**
	 * @param resourcePath the servicePath to set
	 */
	public void setResourcePath(String resourcePath);
	/**
	 * @return the requestFactory
	 */
	public RestClientRequestFactory getRequestFactory();

	/**
	 * @param requestFactory the requestFactory to set
	 */
	public void setRequestFactory(RestClientRequestFactory requestFactory);

	/**
	 * @return the serializationService
	 */
	public SerializationService getSerializationService();

	/**
	 * @param serializationService the serializationService to set
	 */
	public void setSerializationService(SerializationService serializationService);
	/**
	 * @return the exceptionHandler
	 */
	public DAOExceptionHandler getExceptionHandler();

	/**
	 * @param exceptionHandler the exceptionHandler to set
	 */
	public void setExceptionHandler(DAOExceptionHandler exceptionHandler);

}
