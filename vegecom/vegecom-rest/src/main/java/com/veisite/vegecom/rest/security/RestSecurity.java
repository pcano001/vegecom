package com.veisite.vegecom.rest.security;

public abstract class RestSecurity {
	
	/**
	 * parameter string on request query
	 */
	public static final String APIKEY_PARAMETER = "apiKey";
	public static final String SIGNATURE_PARAMETER = "signature";
	public static final String TIMESTAMP_PARAMETER = "timestamp";
	
	/**
	 * key string for attribute on session
	 */
	public static final String PRINCIPAL_KEY = "principalKey";
	public static final String SECRETSESSION_KEY = "secretApiKey";
	public static final String DELTATIME_KEY = "serverTimeDelta";

}
