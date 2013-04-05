package com.veisite.vegecom.rest.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.veisite.vegecom.service.SerializationService;

public class RestExceptionHandler extends AbstractHandlerExceptionResolver {

	@Autowired
    private SerializationService serializationService = null;
	
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		logger.debug("Resolving exception {}",ex.getMessage());
		
		return null;
	}

}
