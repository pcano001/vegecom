package com.veisite.vegecom.rest.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.codec.Base64;

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
	
	/**
	 * crypto secret Key generator
	 */
	private static KeyGenerator keyGen = null;
	

	/**
	 * Generate a Base64 string representing a 256 bits 
	 * "HmacSHA256" algorithm SecretKey
	 * 
	 * @param seed
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateSecretKey(String seed) 
			throws NoSuchAlgorithmException {
		if (keyGen==null) keyGen = KeyGenerator.getInstance(macAlgorithm);
		String nSeed = seed + Long.toString(new Date().getTime()); 
		SecureRandom sr = new SecureRandom(nSeed.getBytes());
		keyGen.init(sr);
	    return Base64.encodeToString(keyGen.generateKey().getEncoded());
	}
	
	/**
	 * Return a Base64 String representing the signature of the message with
	 * the Base64 encondig secretKey.
	 * Algorithm  HmacSHA256
	 * 
	 * @param message
	 * 		The message to sign
	 * @param secretKey
	 * 		Base64 string that represents a 256 bits "HmacSHA256" SecretKey
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static String getSignature(String message, String secretKey) 
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKey key = new SecretKeySpec(Base64.decode(secretKey.getBytes()), macAlgorithm);
		 Mac mac = Mac.getInstance(macAlgorithm);
         mac.init(key);
         byte[] hmacData = mac.doFinal(message.getBytes());
		return Base64.encodeToString(hmacData);
	}
	
	public static String generateApiKeyToken(String seed) {
		String nSeed = seed + Long.toString(new Date().getTime()); 
		SecureRandom sr = new SecureRandom(nSeed.getBytes());
		byte[] token = new byte[16];
		sr.nextBytes(token);
		return Base64.encodeToString(token);
	}
	
}
