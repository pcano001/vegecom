package com.veisite.vegecom.rest.client;

import org.springframework.http.client.ClientHttpRequestFactory;

import com.veisite.vegecom.rest.security.RestSecurityService;

public interface RestClientContext {
	
	public String getScheme();
	
	public void setScheme(String scheme);
	
	public String getHost();
	
	public void setHost(String host);
	
	public int getPort();
	
	public void setPort(int port);
	
	public String getApiPath();
	
	public void setApiPath(String apiPath);
	
	public ClientHttpRequestFactory getClientHttpRequestFactory();
	
	public void setClientHttpRequestFactory(ClientHttpRequestFactory httpFactory);
	
	public RestSecurityService getRestSecurityService();
	
	public void setRestSecurityService(RestSecurityService restSecurityService);
	
}
