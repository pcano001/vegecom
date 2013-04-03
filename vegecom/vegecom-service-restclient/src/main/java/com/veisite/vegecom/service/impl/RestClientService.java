package com.veisite.vegecom.service.impl;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestClientService {

	private String baseURL;
	
	private ClientHttpRequestFactory httpFactory;

	public RestClientService() {
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	public ClientHttpRequestFactory getHttpFactory() {
		return httpFactory;
	}

	public void setHttpFactory(ClientHttpRequestFactory httpFactory) {
		this.httpFactory = httpFactory;
	}

	public RestTemplate createRestTemplate() {
		return new RestTemplate(httpFactory);
	}

}
