package com.veisite.vegecom.service.impl;

import org.springframework.stereotype.Service;

import com.veisite.vegecom.service.security.SecurityService;
import com.veisite.vegecom.service.security.SessionExpirationListener;

/**
 * Implementing NOP security service
 * 
 * @author josemaria
 *
 */
@Service
public class SecurityServiceImpl implements SecurityService {
	
	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public String getAuthenticatedUser() {
		return "testUser";
	}

	@Override
	public String getSessionId() {
		return "testSessionId";
	}

	@Override
	public boolean isValidSession() {
		return true;
	}

	@Override
	public void login(String user, String password) throws Throwable {
	}

	@Override
	public void logout() {
	}

	@Override
	public void touchSession() {
	}
	
	@Override
	public boolean hasRole(String role) {
		return true;
	}

	@Override
	public void addSessionExpirationListener(SessionExpirationListener listener) {
	}

	@Override
	public void removeSessionExpirationListener(
			SessionExpirationListener listener) {
	}

}
