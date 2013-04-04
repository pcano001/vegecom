package com.veisite.vegecom.server.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.utils.dataio.OutputFlowProviderRunnable;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.rest.server.ObjectFlowWriter;
import com.veisite.vegecom.service.MunicipioService;
import com.veisite.vegecom.service.ProvinciaService;
import com.veisite.vegecom.service.SerializationService;

@Controller
@RequestMapping("/municipio")
public class MunicipioController extends DefaultController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * services
	 */
	private MunicipioService dataService;
	private ProvinciaService provService;
	private SerializationService serializationService;
	
	@Inject
	public MunicipioController(MunicipioService dataService, ProvinciaService provService, 
							SerializationService serializationService) {
		this.dataService = dataService;
		this.provService = provService;
		this.serializationService = serializationService;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public @ResponseBody void getList(@RequestParam(required=false) String provinciaId, 
									  HttpServletResponse response) throws Throwable,IOException {
		logger.debug("Requesting municipio list with provincId='{}'",provinciaId);
		Provincia p = null;
		if (provinciaId!=null) {
			p = provService.getById(provinciaId);
			if (p==null) {
				logger.debug("Province with id '{}' not found, returning 404.",provinciaId);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().println("provincia with id='"+provinciaId+"' not found.");
				return;
			}
		}
		final Provincia provincia = p;
		OutputFlowProviderRunnable<Municipio> provider = new OutputFlowProviderRunnable<Municipio>() {
			@Override
			public void doWrite() {
				try {
					dataService.getList(output, provincia);
				} catch (Throwable t) {
					error = t;
				}
			}
		};
		ObjectFlowWriter<Municipio> fw =
				new ObjectFlowWriter<Municipio>(serializationService, provider, response.getOutputStream());
		fw.doWrite();
		logger.debug("Requesting municipio list returned successfully.");
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public @ResponseBody void  getById(HttpServletResponse response, @PathVariable String id) throws IOException {
		logger.debug("Requesting municipio with id='{}'",id);
		Municipio o = dataService.getById(id);
		if (o==null) {
			logger.debug("Municipio with id '{}' not found, returning 404.",id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("municipio with id='"+id+"' not found.");
		} else {
			serializationService.write(response.getOutputStream(), o);
			logger.debug("Requesting municipio returned successfully.");
		}
	}
	
}
