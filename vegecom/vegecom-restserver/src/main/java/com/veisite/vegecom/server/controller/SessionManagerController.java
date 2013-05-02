package com.veisite.vegecom.server.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.vegecom.rest.ClientRestException;
import com.veisite.vegecom.rest.RestException;
import com.veisite.vegecom.rest.ServerRestException;
import com.veisite.vegecom.rest.security.RestApiKeySession;
import com.veisite.vegecom.rest.security.RestLoginFailedException;
import com.veisite.vegecom.rest.security.RestSecurity;
import com.veisite.vegecom.service.SerializationService;
import com.veisite.vegecom.service.security.SecurityService;

@Controller
@RequestMapping(value="/apiKey", produces="application/json")
public class SessionManagerController extends DefaultController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * services
	 */
	private SecurityService securityService;
	private SerializationService serializationService;
	
	@Inject
	public SessionManagerController(SecurityService securityService, 
							SerializationService serializationService) {
		this.securityService = securityService;
		this.serializationService = serializationService;
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public @ResponseBody void create(HttpServletRequest request, HttpServletResponse response)
			throws RestException {
		logger.debug("Requesting new apiKey creation");
		// Comprobamos que la petición viene por un canal seguro.
		if (!request.isSecure()) {
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("New api keys must be requested in an SSL channel."));
		}
		// Tomemos el timestamp del servidor
		Long ms = new Date().getTime();
		Long sTimestamp = Math.round(((double)ms) / 1000.0d);
		
		// Obtenemos los parametros necesarios de la petición (usuario y contraseña)
		String user = request.getParameter(RestSecurity.USER_PARAMETER);
		String password = request.getParameter(RestSecurity.PASSWORD_PARAMETER);
		String cliTime = request.getParameter(RestSecurity.TIMESTAMP_PARAMETER);
		Long cTimestamp = null;
		if (cliTime!=null) {
			try {
				cTimestamp = Long.parseLong(cliTime);
			} catch (NumberFormatException e) {
				logger.debug("Invalid timestamp value from client: {}",cliTime);
			}
		}
		
		if (user==null || password==null) {
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("Invalid user or password"));
		}
		if (cTimestamp==null) {
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("A valid TimeStamp from client is required."));
		}
		Long delta = sTimestamp-cTimestamp;
		
		// Validamos el usuario
		try {
			securityService.login(user, password);
		} catch (Throwable t) {
			throw new RestLoginFailedException("Login failed. Invalid user or password",t);
		}
		// Valid user. Generate secret an create session.
	    String secret = null;
	    try {
			secret = RestSecurity.generateSecretKey(user);
	    } catch (NoSuchAlgorithmException e) {
			throw new ServerRestException(e);
		}
	    DefaultSessionContext sContext = new DefaultSessionContext();
	    sContext.setHost(request.getRemoteAddr());
	    Session session = SecurityUtils.getSecurityManager().start(sContext);
	    String token = (String) session.getId();
	    session.setAttribute(RestSecurity.PRINCIPAL_KEY, user);
	    session.setAttribute(RestSecurity.SECRETSESSION_KEY, secret);
	    session.setAttribute(RestSecurity.DELTATIME_KEY, delta);
	    String m = "Started new session '"+token+"' for user '"+user+"' with delta "+delta+"s.";
	    logger.info(m);
	    
	    // Crear token de sesion de vuelta al cliente
	    RestApiKeySession sObject = new RestApiKeySession(token,secret);
	    try {
	    	fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), sObject);
		} catch (Throwable t) {
			throw new ServerRestException(t);
		}
		logger.debug("apiKey session returned successfully");
	}
	
}
