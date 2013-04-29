package com.veisite.vegecom.rest.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RestRequestParser {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String apiKey = null;
	
	private String signature = null;
	
	private Long timestamp = null;
	
	private String stringToBeSigned = null;
	
	public RestRequestParser(HttpServletRequest request) {
		Assert.notNull(request);
		analize(request);
	}
	
	/**
	 * Analiza la petición extrayendo los parametros necesarios y configurando
	 *  
	 */
	private void analize(HttpServletRequest request) {
		/*
		 * copy all parameters name from query string except signature string
		 */
		Enumeration<String> en = request.getParameterNames();
		Map<String,String> pMap = new HashMap<String,String>();
		while (en.hasMoreElements()) {
			String pa = en.nextElement();
			if (!pa.equals(RestSecurity.SIGNATURE_PARAMETER)) 
				pMap.put(new String(pa).toLowerCase(), pa);
			else this.signature = request.getParameter(RestSecurity.SIGNATURE_PARAMETER);
			if (pa.equals(RestSecurity.APIKEY_PARAMETER)) 
				this.apiKey = request.getParameter(RestSecurity.APIKEY_PARAMETER);
			if (pa.equals(RestSecurity.TIMESTAMP_PARAMETER)) {
				String v = request.getParameter(RestSecurity.TIMESTAMP_PARAMETER);
				try {
					this.timestamp = Long.parseLong(v);
				} catch (NumberFormatException e) {
					logger.warn("Incorrect {} parameter value: expected long and value is {}",
							RestSecurity.TIMESTAMP_PARAMETER,v);
				}
			}
		}
		
		/*
		 * lowercase parameter and order alphabetically
		 * Construct the query again to obtain string to be signed
		 */
		List<String> pl = new ArrayList<String>();
		for (String p : pMap.keySet()) pl.add(p);
		Collections.sort(pl);
		String toSigned = request.getRequestURI();
		for (int i=0;i<pl.size();i++) { 
			toSigned += (i==0? "?" : "&");
			String parameter = pMap.get(pl.get(i));
			String value = request.getParameter(parameter);
			toSigned += pl.get(i)+"="+value;
		}
		this.stringToBeSigned = toSigned; 
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getSignature() {
		return signature;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getStringToBeSigned() {
		return stringToBeSigned;
	}
	
}
