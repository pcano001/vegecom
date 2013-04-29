package com.veisite.vegecom.rest.client;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.veisite.vegecom.rest.client.dao.RestSessionDAO;
import com.veisite.vegecom.rest.security.RestApiKeySession;

public class RestShiroAuthenticator implements Authenticator {
	
	/**
	 * Gestor de sesiones con el servidor
	 */
	private RestSessionDAO sessionDAO;

	@Override
	public AuthenticationInfo authenticate(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		if (!(authenticationToken instanceof UsernamePasswordToken))
			throw new UnsupportedTokenException("User and password must be provided for authentication.");
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		RestApiKeySession apiKey = sessionDAO.createSession(token.getUsername(), new String(token.getPassword()));
		if (apiKey==null) {
			throw new UnauthenticatedException("Could not get session api key from server: null.");
		}
		SimpleAccount account = new SimpleAccount(new SimplePrincipalCollection(token.getUsername(),"default"),
				apiKey);
		return account;
	}

	/**
	 * @return the sessionDAO
	 */
	public RestSessionDAO getSessionDAO() {
		return sessionDAO;
	}

	/**
	 * @param sessionDAO the sessionDAO to set
	 */
	public void setSessionDAO(RestSessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

}
