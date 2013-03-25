package com.veisite.vegecom.server.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.utils.dataio.OutputFlowProviderRunnable;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.service.MunicipioService;
import com.veisite.vegecom.service.SerializationService;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {
	
	/**
	 * services
	 */
	private MunicipioService dataService;
	private SerializationService serializationService;
	
	@Inject
	public MunicipioController(MunicipioService dataService, SerializationService jsonService) {
		this.dataService = dataService;
		this.serializationService = jsonService;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public @ResponseBody void getList2(HttpServletResponse response) throws IOException {
		OutputFlowProviderRunnable<Municipio> provider = new OutputFlowProviderRunnable<Municipio>() {
			@Override
			public void run() {
				try {
					dataService.getList(output);
				} catch (Throwable t) {
					output.close();
					error = t;
				}
			}
		};
		ObjectFlowWriter<Municipio> fw =
				new ObjectFlowWriter<Municipio>(serializationService, provider, response.getOutputStream());
		fw.doWrite();
	}

}
