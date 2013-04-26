package com.veisite.vegecom.rest.security;

public class RestApiKeySession {
	
	private String apiKey;
	
	private String secret;

	public RestApiKeySession(String apiKey, String secret) {
		this.setApiKey(apiKey);
		this.setSecret(secret);
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

}
