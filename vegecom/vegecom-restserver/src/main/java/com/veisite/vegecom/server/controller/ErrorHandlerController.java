package com.veisite.vegecom.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.veisite.vegecom.rest.RestException;
import com.veisite.vegecom.rest.RestInvalidApiPathException;
import com.veisite.vegecom.rest.ServerRestException;
import com.veisite.vegecom.rest.security.RestSecurityException;

@Controller
public class ErrorHandlerController {

	/**
	 * Metodo que reporta un error de autenticacion. Se limita
	 * a lanzar la excepcion 
	 * 
	 * @param request
	 * @param response
	 * @throws RestException
	 */
	@RequestMapping(value="/securityError")
	public @ResponseBody void error(HttpServletRequest request, HttpServletResponse response) throws RestException {
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		if (throwable!=null) {
			if (throwable instanceof RestSecurityException) throw (RestSecurityException)throwable;
			if (throwable instanceof RestException) throw (RestException)throwable;
			throw new ServerRestException(throwable);
		}
		throw new ServerRestException(new Exception("ErrorHandlerController: unexpected servlet exception is null"));
	}

	
	@RequestMapping(value="/notFoundError")
	public @ResponseBody void notFound(HttpServletRequest request, HttpServletResponse response) throws RestException {
		throw new RestInvalidApiPathException("Resource api path invalid");	
	}

	@RequestMapping(value="/restServerError")
	public @ResponseBody void restServerError(HttpServletRequest request, HttpServletResponse response) throws RestException {
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		if (throwable!=null) {
			if (throwable instanceof ServerRestException) throw (ServerRestException)throwable;
			if (throwable instanceof RestException) throw (RestException)throwable;
			throw new ServerRestException(throwable);
		}
		throw new ServerRestException(new Exception("ErrorHandlerController: unexpected servlet exception is null"));
	}

}
