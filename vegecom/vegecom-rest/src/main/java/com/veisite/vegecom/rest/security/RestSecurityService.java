package com.veisite.vegecom.rest.security;

/**
 * Servicio de seguridad Rest que devuelve los datos de acceso a un
 * servicio Rest necesarios para completar una petición al servidor.
 * 
 * @author josemaria
 *
 */
public interface RestSecurityService {

	public String getRestApiKey();
	
	public String getRestSecret();
	
	public String getAuthenticatedUser();
	
}
