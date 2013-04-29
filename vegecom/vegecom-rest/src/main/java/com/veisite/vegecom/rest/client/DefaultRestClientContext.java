package com.veisite.vegecom.rest.client;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.veisite.vegecom.rest.security.RestSecurityService;

public class DefaultRestClientContext implements RestClientContext {
	
	/**
	 * scheme
	 */
	private String scheme = "https";
	
	/**
	 * host
	 */
	private String host = "localhost";
	
	/**
	 * port
	 */
	private int port = 443; 
	
	/**
	 * apiPath
	 */
	private String apiPath = "";
	
	/**
	 * httpFactory
	 */
	private ClientHttpRequestFactory httpFactory;

	/**
	 * restSecurityService
	 */
	private RestSecurityService restSecurityService;

	
	public DefaultRestClientContext() {
		HttpComponentsClientHttpRequestFactory factory = 
				new HttpComponentsClientHttpRequestFactory();
		SchemeRegistry scheme = 
				factory.getHttpClient().getConnectionManager().getSchemeRegistry();
		scheme.register(trustSelfSignedCerts());
		this.httpFactory = factory;
	}

	@Override
	public String getScheme() {
		return scheme;
	}

	@Override
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String getApiPath() {
		return apiPath;
	}

	@Override
	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}

	@Override
	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		return httpFactory;
	}

	@Override
	public void setClientHttpRequestFactory(ClientHttpRequestFactory httpFactory) {
		this.httpFactory = httpFactory;
	}

	@Override
	public RestSecurityService getRestSecurityService() {
		return restSecurityService;
	}

	@Override
	public void setRestSecurityService(RestSecurityService restSecurityService) {
		this.restSecurityService = restSecurityService;
	}

	private Scheme trustSelfSignedCerts(){  
        try {                 
             return new Scheme("https", 8443, 
            		 new SSLSocketFactory(new TrustSelfSignedStrategy(), 
            				 			  new AllowAllHostnameVerifier()));  
        } catch (Exception e) {  
             e.printStackTrace();  
             return null;  
        }  
   }  
	
}
