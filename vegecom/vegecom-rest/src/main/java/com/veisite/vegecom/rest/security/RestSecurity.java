package com.veisite.vegecom.rest.security;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

public abstract class RestSecurity {
	
	/**
	 * parameter string on request query
	 */
	public static final String APIKEY_PARAMETER = "apiKey";
	public static final String SIGNATURE_PARAMETER = "signature";
	public static final String TIMESTAMP_PARAMETER = "timestamp";
	public static final String USER_PARAMETER = "user";
	public static final String PASSWORD_PARAMETER = "password";
	
	/**
	 * key string for attribute on session
	 */
	public static final String PRINCIPAL_KEY = "principalKey";
	public static final String SECRETSESSION_KEY = "secretApiKey";
	public static final String DELTATIME_KEY = "serverTimeDelta";
	
	/**
	 * crypto algorithm 
	 */
	public static final String macAlgorithm = "HmacSHA256";
	
	public static Mac getMacAlgorithm() throws NoSuchAlgorithmException {
		return Mac.getInstance(macAlgorithm);
	}
	
	public static SecretKey generateSecretKey() {
		SecretKeyFactory kf = SecretKeyFactory.getInstance(macAlgorithm);
	    SecretKey passwordKey = kf.generateSecret(keySpec);
	}

}
