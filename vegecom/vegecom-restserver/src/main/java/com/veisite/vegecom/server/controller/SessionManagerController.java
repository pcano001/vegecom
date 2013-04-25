package com.veisite.vegecom.server.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.rest.RestClientException;
import com.veisite.vegecom.rest.RestException;
import com.veisite.vegecom.rest.RestServerException;
import com.veisite.vegecom.rest.security.RestSecurity;
import com.veisite.vegecom.rest.security.RestUnauthorizedException;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.service.SerializationMappingException;
import com.veisite.vegecom.service.SerializationParseException;
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
	
	@RequestMapping(value="", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void create(HttpServletRequest request, HttpServletResponse response)
			throws RestException {
		logger.debug("Requesting new apiKey creation");
		// Comprobamos que la petición viene por un canal seguro.
		if (!request.isSecure()) {
			throw new RestClientException(
				new InvalidDataAccessApiUsageException("New sessions must be requested in an SSL channel."));
		}
		// Obtenemos los parametros necesarios de la petición (usuario y contraseña)
		String user = request.getParameter(RestSecurity.USER_PARAMETER);
		String password = request.getParameter(RestSecurity.PASSWORD_PARAMETER);
		if (user==null || password==null) {
			throw new RestClientException(
				new InvalidDataAccessApiUsageException("Invalid user or password"));
		}
		// Validamos el usuario
		try {
			securityService.login(user, password);
		} catch (Throwable t) {
			throw new RestUnauthorizedException("Login failed. Invalid user or password");
		}
		Cliente o = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (SerializationParseException spe) {
			throw new RestClientException(spe);
		} catch (SerializationMappingException me) {
			throw new RestClientException(me);
		} catch (Throwable t) {
			throw new RestServerException(t);
		}
		if (o==null) {
			logger.error("create. Deserialization: can not get a valid cliente from request body. Null");
			throw new RestClientException(
				new InvalidDataAccessApiUsageException("parameter Cliente is null. Cannot create"));
		}
		if (o.getId()!=null) {
			// Se quiere crear un cliente pero ya trae un id. Error
			logger.debug("create. Cliente error: cliente Id for new cliente must be null.");
			throw new RestClientException(
				new InvalidDataAccessApiUsageException("new Cliente Id is not null. Cannot create"));
		}
		try {
			// Dar de alta el cliente
			o = securityService.save(o);
			// Se devuelve en la respuesta el nuevo cliente con su id.
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), o);
		} catch (Throwable t) {
			throw new RestServerException(t);
		}
		logger.debug("create Cliente: new cliente returned successfully.");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT, consumes="application/json")
	public @ResponseBody void update(HttpServletRequest request, HttpServletResponse response,  
			@PathVariable Long id) throws RestException {
		logger.debug("Requesting cliente update");
		Cliente o = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (SerializationParseException spe) {
			throw new RestClientException(spe);
		} catch (SerializationMappingException me) {
			throw new RestClientException(me);
		} catch (Throwable t) {
			throw new RestServerException(t);
		}
		if (o==null) {
			logger.error("update. Deserialization: can not get a valid cliente from request body. Null");
			throw new RestClientException(
				new InvalidDataAccessApiUsageException("parameter Cliente is null. Cannot update"));
		}
		if (!id.equals(o.getId())) {
			// El id del cliente a actualizar es distinto del recurso accedido
			logger.debug("update. cliente Id mismatch resource id.");
			throw new RestClientException(
				new InvalidDataAccessApiUsageException("Cliente Id mismatch resource Id. Cannot update"));
		}
		try {
			// Actualizar el cliente
			o = securityService.save(o);
			// Se devuelve en la respuesta el cliente actualizado.
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), o);
		} catch (Throwable t) {
			throw new RestServerException(t);
		}
		logger.debug("update Cliente: updated cliente returned successfully.");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public @ResponseBody void delete(HttpServletResponse response, @PathVariable Long id) 
				throws RestException {
		logger.debug("Requesting cliente delete id={}",id);
		Cliente o = null;
		try {
			// Borrar el cliente
			o = securityService.remove(id);
			if (o==null) throw new RestClientException(
					new DataRetrievalFailureException("delete Cliente: not found"));
			// Se devuelve en la respuesta el cliente borrado.
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), o);
		} catch (RestException re) {
			throw re;
		} catch (Throwable t) {
			throw new RestServerException(t);
		}
		logger.debug("delete Cliente: deleted cliente returned successfully.");
	}
	
}
