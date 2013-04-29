package com.veisite.vegecom.rest.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.veisite.vegecom.rest.security.RestSecurity;
import com.veisite.vegecom.rest.security.RestSecurityService;


public class RestClientRequest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * Rest context for setting default values
	 */
	private RestClientContext context;
	
	/**
	 * builder asist for url contruct
	 */
	private URIBuilder uriBuilder = new URIBuilder();
	
	/**
	 * RestTemplate from spring framework thah manage rest request 
	 */
	private RestTemplate restTemplate;
	

	public RestClientRequest(RestClientContext context) {
		Assert.notNull(context);
		this.context = context;
		uriBuilder.setScheme(context.getScheme());
		uriBuilder.setHost(context.getHost());
		uriBuilder.setPort(context.getPort());
		// Construimos RestTemplate
		restTemplate = new RestTemplate(context.getClientHttpRequestFactory());
		ClientHttpRequestInterceptor requestInterceptor =
				new CustomHeaderHttpRequestInterceptor();
		restTemplate.setInterceptors(Collections.singletonList(requestInterceptor));
	}
	
	public void setScheme(String scheme) {
		uriBuilder.setScheme(scheme);
	}
	
	public void setPath(String path) {
		uriBuilder.setPath(context.getApiPath()+path);
	}
	
	public void setParameter(String param, String value) {
		uriBuilder.setParameter(param, value);
	}
	
	public <T> T execute(HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		URI uri;
		try {
			addSecurityContext();
			uri = uriBuilder.build();
		} catch (URISyntaxException use) {
			throw new ResourceAccessException("URI syntax error \"" + uriBuilder.toString() + "\":" + use.getMessage());
		} catch (InvalidKeyException e) {
			throw new ResourceAccessException("Error signing query \"" + uriBuilder.toString() + "\":" + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new ResourceAccessException("Error signing query \"" + uriBuilder.toString() + "\":" + e.getMessage());
		}
		logger.debug("Sending query to server: \"{}\".",uri.toString());
		return restTemplate.execute(uri, method, requestCallback, responseExtractor);
	}
	

	/**
	 * Añade la información necesaria de seguridad en la petición al servidor.
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	private void addSecurityContext() throws InvalidKeyException, NoSuchAlgorithmException {
		RestSecurityService ss = context.getRestSecurityService();
		logger.debug("adding security information: "+ss.toString());
		logger.debug("  user: {}",ss.getAuthenticatedUser());
		logger.debug("  apiKey: {}",ss.getRestApiKey());
		if (ss!=null && ss.getAuthenticatedUser()!=null && 
				ss.getRestApiKey()!=null && ss.getRestSecret()!=null) {
			Long ms = new Date().getTime();
			String timeStamp = Long.toString(Math.round(((double)ms) / 1000.0d));
			uriBuilder.addParameter(RestSecurity.APIKEY_PARAMETER, ss.getRestApiKey());
			uriBuilder.addParameter(RestSecurity.TIMESTAMP_PARAMETER, timeStamp);
			uriBuilder.addParameter(RestSecurity.SIGNATURE_PARAMETER, 
					getSignature(uriBuilder,ss.getRestSecret()));
		}
	}
	
	private String getSignature(URIBuilder uriBuilder, String secret) 
			throws InvalidKeyException, NoSuchAlgorithmException {
		String toSign = uriBuilder.getPath();
		/*
		 * copy all parameters to make them lower case
		 */
		List<NameValuePair> en = uriBuilder.getQueryParams();
		Map<String,NameValuePair> pMap = new HashMap<String,NameValuePair>();
		List<String> pl = new ArrayList<String>();
		for (NameValuePair pp : en) {
			String plow = new String(pp.getName()).toLowerCase();
			pl.add(plow);
			pMap.put(plow, pp);
		}
		Collections.sort(pl);
		for (int i=0;i<pl.size();i++) { 
			toSign += (i==0? "?" : "&");
			String value = pMap.get(pl.get(i)).getValue();
			toSign += pl.get(i)+"="+value;
		}
		return RestSecurity.getSignature(toSign, secret); 
	}
	
	/**
	 * Content type header filler class  
	 * 
	 * @author josemaria
	 */
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
