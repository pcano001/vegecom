package com.veisite.vegecom.rest.client;

public interface RestClientRequestFactory {
	
	public RestClientRequest createRequest();

	public RestClientRequest createRequest(String path);

	public RestClientRequest createRequest(RestClientContext context);
	
	public RestClientRequest createRequest(RestClientContext context, String path);
	
}
