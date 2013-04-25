package com.veisite.vegecom.server.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.ProvinciaService;
import com.veisite.vegecom.service.SerializationService;

@Controller
@RequestMapping("/rs/provincia")
public class ProvinciaController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * services
	 */
	private ProvinciaService dataService;
	private SerializationService serializationService;
	
	@Inject
	public ProvinciaController(ProvinciaService dataService, 
							SerializationService serializationService) {
		this.dataService = dataService;
		this.serializationService = serializationService;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public @ResponseBody void getList(HttpServletResponse response) throws IOException {
		logger.debug("Requesting provincia list");
		List<Provincia> lista = dataService.getList();
		serializationService.writeList(response.getOutputStream(), lista);
		logger.debug("Requesting provincia list returned successfully.");
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public @ResponseBody void  getById(HttpServletResponse response, @PathVariable String id) throws IOException {
		logger.debug("Requesting Provincia with id='{}'",id);
		Provincia o = dataService.getById(id);
		if (o==null) {
			logger.debug("Provincia with id '{}' not found, returning 404.",id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("provincia with id='"+id+"' not found.");
		} else {
			serializationService.write(response.getOutputStream(), o);
			logger.debug("Requesting provincia returned successfully.");
		}
	}
	
}
