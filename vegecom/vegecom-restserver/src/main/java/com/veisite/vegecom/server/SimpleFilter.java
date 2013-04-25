package com.veisite.vegecom.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class SimpleFilter extends OncePerRequestFilter {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Enumeration<String> en = request.getParameterNames();
		List<String> p = new ArrayList<String>();
		while (en.hasMoreElements()) {
			String pa = en.nextElement();
			if (!pa.equals("signature")) p.add(new String(pa));
		}
		Collections.sort(p);
		String toSigned = request.getRequestURI();
		for (int i=0;i<p.size();i++) { 
			logger.debug("Parametros: {}",p.get(i));
			toSigned += (i==0? "?" : "&");
			String v = request.getParameter(p.get(i));
			toSigned += p.get(i).toLowerCase()+"="+v;
		}
		logger.debug("To signed: {}", toSigned);
		
		filterChain.doFilter(request, response);
	}

}
