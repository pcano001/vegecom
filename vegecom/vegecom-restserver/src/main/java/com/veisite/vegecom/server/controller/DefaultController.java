package com.veisite.vegecom.server.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class DefaultController {

	protected void fillResponseHeader(HttpServletResponse response, String contentType) {
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setContentType(contentType+";charset=UTF-8");
	}
	
}
