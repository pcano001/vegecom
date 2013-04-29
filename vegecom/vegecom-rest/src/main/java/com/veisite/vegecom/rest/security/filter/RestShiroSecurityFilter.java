package com.veisite.vegecom.rest.security.filter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.StoppedSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.veisite.vegecom.rest.ServerRestException;
import com.veisite.vegecom.rest.security.RestExpiredSessionException;
import com.veisite.vegecom.rest.security.RestInvalidSessionException;
import com.veisite.vegecom.rest.security.RestSecurity;
import com.veisite.vegecom.rest.security.RestSecurityException;
import com.veisite.vegecom.rest.security.RestUnauthenticatedException;
import com.veisite.vegecom.rest.security.RestRequestParser;

/**
 * Filtro de seguridad que analiza los parámetros de entrada y
 * configura el entorno de Shiro con la información de seguridad
 * necesaria para la petición.
 * 
 * Comprueba los parametros siguientes:	
 * 	apiKey
 *  timestamp
 *  signature
 *  
 *  Si están presentes se supone que es la información de seguridad
 *  necesaria para el acceso bajo una sesión ya iniciada.
 *  
 *   En caso de exito configura correctamente el subject en Shiro de forma
 *   que la llamada SecurityUtils.getSubject() devuelva los datos
 *   de seguridad apropiados. 
 * 
 * @author josemaria
 *
 */
public class RestShiroSecurityFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * Reporta un error en la respuesta y para la ejecución de la petición si
	 * detecta un error de autenticación o seguridad.
	 * Si false, sigue la ejecución.
	 */
	private boolean blockOnError = true;

	/**
	 * Delta maximo en segundos que se permite entre el timestamp
	 * del cliente y la hora del servidor. Se tiene en consideracion la diferencia
	 * horario entre servidor y cliente reportada en la solicitud de la sesión.  
	 */
	private int maxDelta = 5;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.debug("Checking new request: {}",request.getContextPath());
		// Registramos la hora del servidor en segundos
		Long ms = new Date().getTime();
		Long sTimeStamp = Math.round(((double)ms) / 1000.0d);
		
		// Analizamos la petición en busca de los parámetros
		RestRequestParser parser = new RestRequestParser(request);
		if (parser.getApiKey()==null || parser.getTimestamp()==null || 
				parser.getSignature()==null || parser.getStringToBeSigned()==null) {
			logger.debug("Invalid request security format.");
			riseSecurityError(new RestUnauthenticatedException("Invalid security information on request"),
					request, response, filterChain);
			return;
		}
			
		// Request formally correct, analize session
		logger.debug("Request is a formaly valid secure request. " +
					"apiKey={}, timestamp={}",
					parser.getApiKey(),parser.getTimestamp());
			
		Session session = null;
		RestSecurityException sError = null;
		String secret = null;
		Long delta = null;
		String user = null;
		try {
			DefaultSessionKey sk = new DefaultSessionKey(parser.getApiKey());
			session = SecurityUtils.getSecurityManager().getSession(sk);
			secret = (String) session.getAttribute(RestSecurity.SECRETSESSION_KEY);
			delta = (Long) session.getAttribute(RestSecurity.DELTATIME_KEY);
			user = (String) session.getAttribute(RestSecurity.PRINCIPAL_KEY);
		} catch (StoppedSessionException ise) {
			sError = new RestExpiredSessionException("Session has expired or is stopped");
		} catch (SessionException se) {
			// == session is null 
		}
		if (session==null || secret==null || delta==null || user==null) {
			if (sError==null) sError = new RestInvalidSessionException("Session not found or invalid");
			riseSecurityError(sError, request, response, filterChain);
			return;
		}

		// We have a valid session. Test a valid timestamp
		Long timeDistance = Math.abs(sTimeStamp-parser.getTimestamp()-delta);
		if (timeDistance > maxDelta) {
			// timestamp is not in range
			logger.debug("Invalid security request: timestamp distance {} is not in range.",timeDistance);
			riseSecurityError(new RestUnauthenticatedException("Invalid security information on request. Invalid timestamp"), 
					request, response, filterChain);
			return;
		}
		// Now test a valid signature
		String sign = null;
		try {
			sign = RestSecurity.getSignature(parser.getStringToBeSigned(), secret);
		} catch (InvalidKeyException e) {
			throw new ServerRestException("Server error getting signature from query.");
		} catch (NoSuchAlgorithmException e) {
			throw new ServerRestException("Server error getting signature from query.");
		}
		if (!parser.getSignature().equals(sign)) {
			logger.debug("Invalid security request: sign missmatch.");
			riseSecurityError(new RestUnauthenticatedException("Invalid security information on request. Sign missmatch"), 
					request, response, filterChain);
			return;
		}
		
		// Request is correctly authenticated
		DefaultSubjectContext subjectContext = new DefaultSubjectContext();
		subjectContext.setPrincipals(new SimplePrincipalCollection(user,"default"));
		subjectContext.setAuthenticated(true);
		subjectContext.setSecurityManager(SecurityUtils.getSecurityManager());
		subjectContext.setSessionId(parser.getApiKey());
		subjectContext.setSession(session);
		subjectContext.setSessionCreationEnabled(false);
		
		Subject subject = SecurityUtils.getSecurityManager().createSubject(subjectContext);
		ThreadContext.bind(subject);
		session.touch();
		
		filterChain.doFilter(request, response);
	}
	
	
	private void riseSecurityError(RestSecurityException error, HttpServletRequest request, 
			HttpServletResponse response,  FilterChain filterChain) throws ServletException, IOException {
		if (isBlockOnError()) throw error;
		logger.debug("SecurityError. Not blokcing: {}.",error.getMessage());
		filterChain.doFilter(request, response);
	}
	
	
	public boolean isBlockOnError() {
		return blockOnError;
	}

	public void setBlockOnError(boolean blockOnError) {
		this.blockOnError = blockOnError;
	}

	/**
	 * @return the maxDelta
	 */
	public int getMaxDelta() {
		return maxDelta;
	}


	/**
	 * @param maxDelta the maxDelta to set
	 */
	public void setMaxDelta(int maxDelta) {
		this.maxDelta = maxDelta;
	}

}
