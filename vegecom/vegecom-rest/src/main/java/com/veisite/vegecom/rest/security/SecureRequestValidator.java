package com.veisite.vegecom.rest.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class SecureRequestValidator {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String apiKey = null;
	
	private String signature = null;
	
	private Long timestamp = null;
	
	private String stringToBeSigned = null;
	
	public SecureRequestValidator(HttpServletRequest request) {
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
		List<String> pList = new ArrayList<String>();
		while (en.hasMoreElements()) {
			String pa = en.nextElement();
			if (!pa.equals(RestSecurity.SIGNATURE_PARAMETER)) pList.add(new String(pa));
			else this.signature = request.getParameter(RestSecurity.SIGNATURE_PARAMETER);
			if (pa.equals(RestSecurity.APIKEY_PARAMETER)) 
				this.apiKey = request.getParameter(RestSecurity.APIKEY_PARAMETER);
			if (pa.equals(RestSecurity.APIKEY_PARAMETER)) {
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
		for (String p : pList) p.toLowerCase();
		Collections.sort(pList);
		String toSigned = request.getRequestURI();
		for (int i=0;i<pList.size();i++) { 
			toSigned += (i==0? "?" : "&");
			String v = request.getParameter(pList.get(i));
			toSigned += pList.get(i)+"="+v;
		}
		this.stringToBeSigned = toSigned; 
	}

	/**
	 * Test if resquest have all necesary parameters for security 
	 * Do not make any signature test. 
	 * 
	 * @return
	 */
	public boolean isFormalyValidSecureRequest() {
		return (apiKey!=null && timestamp!=null && signature!=null && stringToBeSigned!=null);
	}
	
	/**
	 * Test if signature is correct for the query
	 * using secret key 
	 * 
	 * @return
	 */
	public boolean isCorrectlySigned(String secret) {
		if (!isFormalyValidSecureRequest()) return false;
		return this.signature.equals(getSignature(stringToBeSigned,secret));
	}
	
	
	/*
	 * Make cryptgraphic computation using algorithm HMAC-SHA256
	 * and return signed key
	 */
	public String getSignature(String clearText, String secret) {
		if (clearText!=null && secret!=null) {
			try {
				Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
				SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
				sha256_HMAC.init(secret_key);
				return Base64.encodeToString(sha256_HMAC.doFinal(clearText.getBytes()));
			} catch (NoSuchAlgorithmException e) {
				logger.error("Algorithm HmacSHA256 not available.",e);
			} catch (InvalidKeyException e) {
				logger.error("Invalid secret key for HmacSHA256 Algorithm",e);
			} finally {}
		}
		return null;
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
