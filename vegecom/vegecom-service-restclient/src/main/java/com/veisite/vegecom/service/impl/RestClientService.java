package com.veisite.vegecom.service.impl;

import java.io.IOException;
import java.util.Collections;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
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
		RestTemplate rt = new RestTemplate(httpFactory);
		ClientHttpRequestInterceptor requestInterceptor =
				new CustomHeaderHttpRequestInterceptor();
		rt.setInterceptors(Collections.singletonList(requestInterceptor));
		return rt;
	}
	
	public URIBuilder insertSecurityContext(URIBuilder uriBuilder) {
		return uriBuilder;
	}
	
	
	private class CustomHeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor {
		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body,
				ClientHttpRequestExecution execution) throws IOException {
			HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
		    requestWrapper.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));			
		    requestWrapper.getHeaders().setContentType(MediaType.APPLICATION_JSON);	
		    return execution.execute(requestWrapper, body);
		}
		
	}
	
	

}
