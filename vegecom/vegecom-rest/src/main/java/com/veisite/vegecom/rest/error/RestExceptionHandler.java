package com.veisite.vegecom.rest.error;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.veisite.vegecom.service.SerializationService;

public class RestExceptionHandler extends AbstractHandlerExceptionResolver {

    private SerializationService serializationService = null;
	
    private RestErrorResolver errorResolver;
    
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		logger.debug("Resolving exception {}",ex.getMessage());
		RestError err = errorResolver.resolve(ex);
		response.setStatus(err.getStatus().value());
		response.setContentType("application/json");
		try {
			serializationService.write(response.getOutputStream(), err);
		} catch (IOException e) {
			logger.error("Unexpected IO error handing exception",e);
		}
		return null;
	}

	public SerializationService getSerializationService() {
		return serializationService;
	}

	public void setSerializationService(SerializationService serializationService) {
		this.serializationService = serializationService;
	}

	public RestErrorResolver getErroResolver() {
		return errorResolver;
	}

	public void setErrorResolver(RestErrorResolver errorResolver) {
		this.errorResolver = errorResolver;
	}

}
