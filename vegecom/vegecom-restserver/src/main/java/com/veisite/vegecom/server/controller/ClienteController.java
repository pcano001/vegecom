package com.veisite.vegecom.server.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.utils.dataio.OutputFlowProviderRunnable;
import com.veisite.vegecom.model.Cliente;
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
	public @ResponseBody void  getById(HttpServletResponse response, @PathVariable Long id) throws IOException {
		logger.debug("Requesting cliente with id='{}'",id);
		Cliente o = dataService.getById(id);
		if (o==null) {
			logger.debug("Cliente with id '{}' not found, returning 404.",id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("cliente with id='"+id+"' not found.");
		} else {
			serializationService.write(response.getOutputStream(), o);
			logger.debug("Requesting cliente returned successfully.");
		}
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public @ResponseBody void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("Requesting cliente creation");
		Cliente o = null;
		IOException serExcep = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (IOException ioe) {
			// No se ha podido parsear el cliente a dar de alta. Generar bad request
			logger.debug("Deserialization: IOException error.",ioe);
			serExcep = ioe;
		}
		if (o==null)
			logger.debug("Deserialization: can not get a valid cliente from request body, returning 400.");
		if (o==null || serExcep!=null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("create Cliente error: invalid cliente object on request body.");
			return;
		}
		if (o.getId()!=null) {
			// Se quiere crear un cliente pero ya trae un id. Error
			logger.debug("create Cliente error: cliente Id for new cliente must be null, returning 400.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("create Cliente error: cliente Id for new cliente must be null.");
			return;
		}
		// Dar de alta el cliente
		o = dataService.save(o);
		// Se devuelve en la respuesta el nuevo cliente con su id.
		serializationService.write(response.getOutputStream(), dataService.getById(o.getId()));
		logger.debug("create Cliente: new cliente returned successfully.");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public @ResponseBody void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("Requesting cliente update");
		Cliente o = null;
		IOException serExcep = null;
		try {
			o = serializationService.read(request.getInputStream(), Cliente.class);
		} catch (IOException ioe) {
			// No se ha podido parsear el cliente a actualizar. Generar bad request
			logger.debug("Deserialization: IOException error.",ioe);
			serExcep = ioe;
		}
		if (o==null)
			logger.debug("Deserialization: can not get a valid cliente from request body, returning 400.");
		if (o==null || serExcep!=null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("update Cliente error: invalid cliente object on request body.");
			return;
		}
		// Actualizar el cliente
		try {
			o = dataService.save(o);
		} catch (DataAccessException th) {
			logger.debug("Error updating cliente on cliente service", th);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().println("update Cliente error: error persisting cliente data.");
			response.getWriter().println(th.getMessage());
			th.printStackTrace(response.getWriter());
			return;
		}
		// Se devuelve en la respuesta el cliente actualizado.
		serializationService.write(response.getOutputStream(), o);
		logger.debug("update Cliente: updated cliente returned successfully.");
	}
	
}
