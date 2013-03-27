package com.veisite.vegecom.rest.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.veisite.vegecom.rest.SerializationService;

public class RestExceptionHandler extends AbstractHandlerExceptionResolver {

	@Autowired
    private SerializationService serializationService = null;
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		return null;
	}

}
