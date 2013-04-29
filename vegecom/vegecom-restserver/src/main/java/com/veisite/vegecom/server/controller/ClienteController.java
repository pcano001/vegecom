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
import com.veisite.vegecom.rest.ClientRestException;
import com.veisite.vegecom.rest.RestException;
import com.veisite.vegecom.rest.ServerRestException;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.service.SerializationMappingException;
import com.veisite.vegecom.service.SerializationParseException;
import com.veisite.vegecom.service.SerializationService;

@Controller
@RequestMapping(value="/rs/cliente", produces="application/json")
public class ClienteController extends DefaultController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * services
	 */
	private ClienteService dataService;
	private SerializationService serializationService;
	
	@Inject
	public ClienteController(ClienteService dataService, 
							SerializationService serializationService) {
		this.dataService = dataService;
		this.serializationService = serializationService;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public @ResponseBody void getList(HttpServletResponse response) throws RestException {
		logger.debug("Requesting cliente list");
		try {
			List<Cliente> l = dataService.getList();
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.writeList(response.getOutputStream(), l);
		} catch (Throwable t) {
			throw new ServerRestException(t);
		}
		logger.debug("Requesting cliente list returned successfully.");
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public @ResponseBody void  getById(HttpServletResponse response, @PathVariable Long id) 
			throws RestException {
		logger.debug("Requesting cliente with id='{}'",id);
		try {
			Cliente o = dataService.getById(id);
			Thread.sleep(2000L);
			if (o!=null) {
				fillResponseHeader(response, serializationService.getContentType());
				serializationService.write(response.getOutputStream(), o);
			} else {
				logger.debug("Cliente {} not found.",id);
				throw new ClientRestException( 
						new DataRetrievalFailureException("Cliente with id="+id+" not found"));
			}
		} catch (RestException re) {
			throw re;
		} catch (Throwable t) {
			throw new ServerRestException(t);
		}
		logger.debug("Requesting cliente returned successfully.");
	}
	
	@RequestMapping(value="", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody void create(HttpServletRequest request, HttpServletResponse response)
			throws RestException {
		logger.debug("Requesting cliente creation");
		Cliente o = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (SerializationParseException spe) {
			throw new ClientRestException(spe);
		} catch (SerializationMappingException me) {
			throw new ClientRestException(me);
		} catch (Throwable t) {
			throw new ServerRestException(t);
		}
		if (o==null) {
			logger.error("create. Deserialization: can not get a valid cliente from request body. Null");
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("parameter Cliente is null. Cannot create"));
		}
		if (o.getId()!=null) {
			// Se quiere crear un cliente pero ya trae un id. Error
			logger.debug("create. Cliente error: cliente Id for new cliente must be null.");
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("new Cliente Id is not null. Cannot create"));
		}
		try {
			// Dar de alta el cliente
			o = dataService.save(o);
			// Se devuelve en la respuesta el nuevo cliente con su id.
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), o);
		} catch (Throwable t) {
			throw new ServerRestException(t);
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
			throw new ClientRestException(spe);
		} catch (SerializationMappingException me) {
			throw new ClientRestException(me);
		} catch (Throwable t) {
			throw new ServerRestException(t);
		}
		if (o==null) {
			logger.error("update. Deserialization: can not get a valid cliente from request body. Null");
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("parameter Cliente is null. Cannot update"));
		}
		if (!id.equals(o.getId())) {
			// El id del cliente a actualizar es distinto del recurso accedido
			logger.debug("update. cliente Id mismatch resource id.");
			throw new ClientRestException(
				new InvalidDataAccessApiUsageException("Cliente Id mismatch resource Id. Cannot update"));
		}
		try {
			// Actualizar el cliente
			o = dataService.save(o);
			// Se devuelve en la respuesta el cliente actualizado.
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), o);
		} catch (Throwable t) {
			throw new ServerRestException(t);
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
			o = dataService.remove(id);
			if (o==null) throw new ClientRestException(
					new DataRetrievalFailureException("delete Cliente: not found"));
			// Se devuelve en la respuesta el cliente borrado.
			fillResponseHeader(response, serializationService.getContentType());
			serializationService.write(response.getOutputStream(), o);
		} catch (RestException re) {
			throw re;
		} catch (Throwable t) {
			throw new ServerRestException(t);
		}
		logger.debug("delete Cliente: deleted cliente returned successfully.");
	}
	
}
