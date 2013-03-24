package com.veisite.vegecom.service.security;


public interface SecurityService {
	
	public boolean isAuthenticated();
	
	public String getAuthenticatedUser();
	
	public String getSessionId();
	
	public boolean isValidSession();
	
	public void touchSession();
	
	public void login(String user, String password) throws Throwable;
	
	public void logout();
	
	public void addSessionExpirationListener(SessionExpirationListener listener);

	public void removeSessionExpirationListener(SessionExpirationListener listener);

	/**
	 * Devuelve true si el usuario de la sesión actual tiene el rol indicado
	 * 
	 * @return
	 */
	public boolean hasRole(String role); 


}
