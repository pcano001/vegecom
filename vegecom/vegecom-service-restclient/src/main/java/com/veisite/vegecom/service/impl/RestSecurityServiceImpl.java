package com.veisite.vegecom.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.rest.security.RestSecurity;
import com.veisite.vegecom.rest.security.RestSecurityService;
import com.veisite.vegecom.rest.security.RestUnauthenticatedException;
import com.veisite.vegecom.service.security.SecurityService;
import com.veisite.vegecom.service.security.SessionExpirationListener;

/**
 * Implementing Shiro security service
 * 
 * @author josemaria
 *
 */
public class RestSecurityServiceImpl implements RestSecurityService, SecurityService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Variables de sesion
	 */
	private Subject subject = null;
	private String authenticatedUser = null;
	private String sessionId=null;
	private String apiKey = null;
	private String secret = null;
	
	@Override
	public boolean isAuthenticated() {
		if (subject==null) return false;
		return subject.isAuthenticated();
	}

	@Override
	public String getAuthenticatedUser() {
		return authenticatedUser;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public boolean isValidSession() {
		if (subject==null) return false;
		try {
			subject.getSession().getAttribute("noAttribute");
		} catch (InvalidSessionException ise) {
			return false;
		}
		return true;
	}


	@Override
	public void login(String user, String password) throws Throwable {
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = 
				new UsernamePasswordToken(user, password);
		currentUser.login(token);
		if (!currentUser.isAuthenticated()) {
			throw new RestUnauthenticatedException("User not authenticated. Unknown cause");
		}
		this.subject = currentUser;
		authenticatedUser = user;
		apiKey = (String) currentUser.getSession().getAttribute(RestSecurity.APIKEY_KEY);
		secret = (String) currentUser.getSession().getAttribute(RestSecurity.SECRETSESSION_KEY);
		logger.debug("User {} has been authenticated with apiKey {}.",user,apiKey);
	}

	@Override
	public void logout() {
		SecurityUtils.getSubject().logout();
	}

	@Override
	public void touchSession() {
		try {
			SecurityUtils.getSubject().getSession().touch();
		} catch (SessionException e) { }
	}
	
	@Override
	public boolean hasRole(String role) {
		return SecurityUtils.getSubject().hasRole(role);
	}

	@Override
	public void addSessionExpirationListener(SessionExpirationListener listener) {
	}

	@Override
	public void removeSessionExpirationListener(
			SessionExpirationListener listener) {
	}

	@Override
	public String getRestApiKey() {
		return apiKey;
	}

	@Override
	public String getRestSecret() {
		return secret;
	}

}
