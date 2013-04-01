package com.veisite.vegecom.rest.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import com.veisite.vegecom.service.SerializationService;

public class RestExceptionHandler extends AbstractHandlerExceptionResolver {

	@Autowired
    private SerializationService serializationService = null;
	
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private RestErrorResolver errorResolver;

    public RestExceptionHandler() {
        this.errorResolver = new DefaultRestErrorResolver();
    }

    public void setErrorResolver(RestErrorResolver errorResolver) {
        this.errorResolver = errorResolver;
    }

    public RestErrorResolver getErrorResolver() {
        return this.errorResolver;
    }

    public SerializationService getSerializationService() {
		return serializationService;
	}

	public void setSerializationService(SerializationService serializationService) {
		this.serializationService = serializationService;
	}

	/**
* Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
* represents a specific error page if appropriate.
* <p/>
* May be overridden in subclasses, in order to apply specific
* exception checks. Note that this template method will be invoked <i>after</i> checking whether this resolved applies
* ("mappedHandlers" etc), so an implementation may simply proceed with its actual exception handling.
*
* @param request current HTTP request
* @param response current HTTP response
* @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception (for example,
* if multipart resolution failed)
* @param ex the exception that got thrown during handler execution
* @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
*/
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        logger.debug("Resolving exception {}.",ex.getMessage());
        
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        RestErrorResolver resolver = getErrorResolver();

        RestError error = resolver.resolveError(webRequest, handler, ex);
        if (error == null) {
            return null;
        }

        ModelAndView mav = null;

        try {
            mav = getModelAndView(webRequest, handler, error);
        } catch (Exception invocationEx) {
            logger.error("Acquiring ModelAndView for Exception [" + ex + "] resulted in an exception.", invocationEx);
        }

        return mav;
    }

    protected ModelAndView getModelAndView(ServletWebRequest webRequest, Object handler, RestError error) throws Exception {

        applyStatusIfPossible(webRequest, error);

        return handleResponseBody(error, webRequest);
    }

    private void applyStatusIfPossible(ServletWebRequest webRequest, RestError error) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(error.getStatus().value());
        }
    }

    private ModelAndView handleResponseBody(RestError error, ServletWebRequest webRequest) throws ServletException, IOException {
    	serializationService.write(webRequest.getResponse().getOutputStream(), error);
        return new ModelAndView();
    }
    
}
