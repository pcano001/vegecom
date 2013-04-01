package com.veisite.vegecom.server.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.utils.dataio.OutputFlowProviderRunnable;
import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.model.exception.DataModelException;
import com.veisite.vegecom.model.exception.InvalidResourceParameterException;
import com.veisite.vegecom.model.exception.NewResourceWithExplicitIdException;
import com.veisite.vegecom.model.exception.ResourceNotFoundException;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.service.SerializationService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	
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
	public @ResponseBody void getList(HttpServletResponse response) throws IOException {
		logger.debug("Requesting cliente list");
		OutputFlowProviderRunnable<Cliente> provider = new OutputFlowProviderRunnable<Cliente>() {
			@Override
			public void run() {
				try {
					dataService.writeListTo(output);
				} catch (Throwable t) {
					output.close();
					error = t;
				}
			}
		};
		ObjectFlowWriter<Cliente> fw =
				new ObjectFlowWriter<Cliente>(serializationService, provider, response.getOutputStream());
		fw.doWrite();
		logger.debug("Requesting cliente list returned successfully.");
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public @ResponseBody void  getById(HttpServletResponse response, @PathVariable Long id) 
			throws IOException, ResourceNotFoundException {
		logger.debug("Requesting cliente with id='{}'",id);
		Cliente o = dataService.getById(id);
		if (o!=null) {
			serializationService.write(response.getOutputStream(), o);
		} else {
			logger.debug("Cliente {} not found.",id);
			throw new ResourceNotFoundException();
		}
		logger.debug("Requesting cliente returned successfully.");
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public @ResponseBody void create(HttpServletRequest request, HttpServletResponse response) 
				throws IOException, DataModelException {
		logger.debug("Requesting cliente creation");
		Cliente o = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (IOException ioe) {
			// No se ha podido parsear el cliente a dar de alta. Generar bad request
			logger.debug("Deserialization: IOException error getting Cliente argument.",ioe);
			throw ioe;
		}
		if (o==null) {
			logger.debug("Deserialization: can not get a valid cliente from request body.");
			throw new InvalidResourceParameterException();
		}
		if (o.getId()!=null) {
			// Se quiere crear un cliente pero ya trae un id. Error
			logger.debug("create Cliente error: cliente Id for new cliente must be null.");
			throw new NewResourceWithExplicitIdException();
		}
		// Dar de alta el cliente
		o = dataService.save(o);
		// Se devuelve en la respuesta el nuevo cliente con su id.
		serializationService.write(response.getOutputStream(), o);
		logger.debug("create Cliente: new cliente returned successfully.");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public @ResponseBody void update(HttpServletRequest request, HttpServletResponse response) 
				throws IOException, DataModelException {
		logger.debug("Requesting cliente update");
		Cliente o = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (IOException ioe) {
			// No se ha podido parsear el cliente a actualizar. Generar bad request
			logger.debug("Deserialization: can not get a valid cliente from request body.");
			throw ioe;
		}
		if (o==null) {
			logger.debug("Deserialization: can not get a valid cliente from request body.");
			throw new InvalidResourceParameterException();
		}
		// Actualizar el cliente
		o = dataService.save(o);
		// Se devuelve en la respuesta el cliente actualizado.
		serializationService.write(response.getOutputStream(), o);
		logger.debug("update Cliente: updated cliente returned successfully.");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public @ResponseBody void delete(HttpServletRequest request, HttpServletResponse response) 
				throws IOException, DataModelException {
		logger.debug("Requesting cliente delete");
		Cliente o = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (IOException ioe) {
			// No se ha podido parsear el cliente a Eliminar. Generar bad request
			logger.debug("Deserialization: can not get a valid cliente from request body.");
			throw ioe;
		}
		if (o==null) {
			logger.debug("Deserialization: can not get a valid cliente from request body.");
			throw new InvalidResourceParameterException();
		}
		// Actualizar el cliente
		dataService.remove(o);
		// Se devuelve en la respuesta el cliente borrado.
		serializationService.write(response.getOutputStream(), o);
		logger.debug("delete Cliente: deleted cliente returned successfully.");
	}
	
}
