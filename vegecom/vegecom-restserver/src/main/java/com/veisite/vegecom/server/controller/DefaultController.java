package com.veisite.vegecom.server.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class DefaultController {

	protected void fillResponseHeader(HttpServletResponse response) {
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setContentType("application/json;charset=UTF-8");
	}
	
}
