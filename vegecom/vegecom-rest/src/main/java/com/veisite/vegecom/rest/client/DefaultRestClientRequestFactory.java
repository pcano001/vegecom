package com.veisite.vegecom.rest.client;

import org.springframework.util.Assert;

public class DefaultRestClientRequestFactory implements
		RestClientRequestFactory {
	
	private RestClientContext context;

	public DefaultRestClientRequestFactory() {
	}

	@Override
	public RestClientRequest createRequest() {
		return createRequest(context, "");
	}

	@Override
	public RestClientRequest createRequest(String path) {
		return createRequest(context, path);
	}

	@Override
	public RestClientRequest createRequest(RestClientContext context) {
		return createRequest(context, "");
	}
	
	@Override
	public RestClientRequest createRequest(RestClientContext context, String path) {
		Assert.notNull(context);
		Assert.notNull(path);
		RestClientRequest request = new RestClientRequest(context);
		request.setPath(path);
		return request;
	}

	/**
	 * @return the context
	 */
	public RestClientContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(RestClientContext context) {
		this.context = context;
	}
	
}
