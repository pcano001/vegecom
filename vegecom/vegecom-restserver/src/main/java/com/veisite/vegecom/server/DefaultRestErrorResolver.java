package com.veisite.vegecom.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.PersistenceException;

import org.hibernate.JDBCException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.veisite.vegecom.rest.ClientRestException;
import com.veisite.vegecom.rest.RestException;
import com.veisite.vegecom.rest.RestInvalidApiPathException;
import com.veisite.vegecom.rest.ServerRestException;
import com.veisite.vegecom.rest.error.RestError;
import com.veisite.vegecom.rest.error.RestErrorCode;
import com.veisite.vegecom.rest.error.RestErrorResolver;
import com.veisite.vegecom.rest.security.RestExpiredSessionException;
import com.veisite.vegecom.rest.security.RestInvalidSessionException;
import com.veisite.vegecom.rest.security.RestLoginFailedException;
import com.veisite.vegecom.rest.security.RestSecurityException;
import com.veisite.vegecom.rest.security.RestUnauthenticatedException;

public class DefaultRestErrorResolver implements RestErrorResolver, MessageSourceAware {
	
	private MessageSource messageSource;

	public DefaultRestErrorResolver() {
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public RestError resolve(Exception ex, Locale locale) {
		RestError err;
		if (ex instanceof RestException) {
			err = resolveRestException((RestException) ex,locale);
			if (err.getExceptionClass()==null) {
				if (ex.getCause()!=null) err.setExceptionClass(ex.getCause().getClass().getSimpleName());
				else err.setExceptionClass(ex.getClass().getSimpleName());
			}
		} else {
			err = unknownError(ex, locale);
			if (err.getExceptionClass()==null) err.setExceptionClass(ex.getClass().getSimpleName());
		}
		if (err.getDebugMessages()==null) err.setDebugMessages(getDebugMessage(ex));
		return err;
	}
	
	private String getMessage(String code, Object [] args, String defaultMessage, Locale locale) {
		if (messageSource==null) return defaultMessage;
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}
	
	private RestError unknownError(Exception ex, Locale locale) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(ex,err,locale);
		if (err.getStatus()==null) err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		if (err.getCode()==0) err.setCode(RestErrorCode.UNKNOW.getValue());
		if (err.getMessage()==null) {
			String m = getMessage(RestErrorCode.UNKNOW.getMessageKey(),null,"Unexpected error", locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {});
			err.setDebugMessages(getDebugMessage(ex));
		}
		return err;
	}
	
	private String[] getDebugMessage(Throwable ex) {
		List<String> ls = new ArrayList<String>();
		Throwable e = ex;
		while (e!=null) {
			ls.add(e.getClass().getSimpleName()+": "+e.getMessage());
			if (e.getCause()!=e) e=e.getCause();
			else break;
		}
		return ls.toArray(new String[]{});
	}
	
	private RestError resolveRestException(RestException re, Locale locale) {
		if (re instanceof ClientRestException)
			return resolveRestClientException((ClientRestException) re, locale);
		if (re instanceof ServerRestException)
			return resolveRestServerException((ServerRestException) re, locale);
		return null;
	}
	
	private RestError resolveRestClientException(ClientRestException rce, Locale locale) {
		if (rce instanceof RestSecurityException) 
			return resolveRestSecurityException((RestSecurityException)rce, locale);
		if (rce instanceof RestInvalidApiPathException) 
			return resolveRestInvalidApiPath((RestInvalidApiPathException)rce, locale);
		RestError err = new RestErrorTemplate();
		resolveInternalException(rce.getCause(), err, locale);
		if (err.getCode() == 0) err.setCode(RestErrorCode.BAD_REQUEST.getValue());
		if (err.getStatus() == null) err.setStatus(HttpStatus.BAD_REQUEST);
		return err;
	}

	private RestError resolveRestServerException(ServerRestException rce, Locale locale) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(rce.getCause(), err, locale);
		if (err.getCode() == 0) err.setCode(RestErrorCode.INTERNAL_SERVER_ERROR.getValue());
		if (err.getStatus() == null) err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return err;
	}
	
	private RestError resolveRestSecurityException(RestSecurityException rce, Locale locale) {
		RestError err = new RestErrorTemplate();
		err.setStatus(HttpStatus.UNAUTHORIZED);
		RestErrorCode ec = RestErrorCode.UNAUTHORIZED_REQUEST;
		if (rce instanceof RestLoginFailedException) ec = RestErrorCode.AUTHENTICATION_FAILED; 
		if (rce instanceof RestUnauthenticatedException) ec = RestErrorCode.UNAUTHENTICATED_REQUEST; 
		if (rce instanceof RestInvalidSessionException) ec = RestErrorCode.SESSION_INVALID; 
		if (rce instanceof RestExpiredSessionException) ec = RestErrorCode.SESSION_EXPIRED; 
		err.setCode(ec.getValue());
		String m = getMessage(ec.getMessageKey(), null, rce.getMessage(), locale);
		err.setMessage(m);
		err.setDetailedMessages(new String[] {rce.getMessage()});
		return err;
	}
	
	private RestError resolveRestInvalidApiPath(RestInvalidApiPathException rce, Locale locale) {
		RestError err = new RestErrorTemplate();
		err.setStatus(HttpStatus.NOT_FOUND);
		RestErrorCode ec = RestErrorCode.PATH_NOT_FOUND;
		err.setCode(ec.getValue());
		String m = getMessage(ec.getMessageKey(), null, rce.getMessage(), locale);
		err.setMessage(m);
		err.setDetailedMessages(new String[] {rce.getMessage()});
		return err;
	}
	
	private void resolveInternalException(Throwable ex, RestError err, Locale locale) {
		if (ex==null) return;
		// Comprobemos si es una excepcion de acceso a datos
		if (ex instanceof DataAccessException) 
			resolveDataAccessException((DataAccessException)ex,err,locale);
		else if (ex instanceof TransactionException) 
			resolveTransactionException((TransactionException)ex,err,locale);
		else if (ex instanceof IOException) 
			resolveIOException((IOException)ex,err,locale);
		else resolveUnhandledException(ex,err,locale);
	}

	private void resolveDataAccessException(DataAccessException ex, RestError err, Locale locale) {
		if (ex instanceof DataRetrievalFailureException) {
			err.setStatus(HttpStatus.NOT_FOUND);
			err.setCode(RestErrorCode.RESOURCE_NOT_FOUND.getValue());
			String m = getMessage(RestErrorCode.RESOURCE_NOT_FOUND.getMessageKey(), null, 
					"Resource not found", locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {ex.getMessage()});
			return;
		}
		if (ex instanceof InvalidDataAccessApiUsageException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(RestErrorCode.INVALID_ARGUMENT.getValue());
			String m = getMessage(RestErrorCode.INVALID_ARGUMENT.getMessageKey(), null, 
					"Illegal arguments on request",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {ex.getMessage()});
			return;
		}
		if (ex instanceof ConcurrencyFailureException) {
			err.setStatus(HttpStatus.CONFLICT);
			err.setCode(RestErrorCode.DATACONCURRENCY_CONFLICT.getValue());
			String m = getMessage(RestErrorCode.DATACONCURRENCY_CONFLICT.getMessageKey(), null, 
					"Update Conflict. It seems that data has been updated or deleted by another session",
					locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {ex.getMessage()});
			return;
		}
		if (ex instanceof DataIntegrityViolationException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(RestErrorCode.DATAINTEGRITY_VIOLATION.getValue());
			String m = getMessage(RestErrorCode.DATAINTEGRITY_VIOLATION.getMessageKey(), null, 
					"Data integrity violation",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {ex.getMessage()});
			return;
		}
		if (ex instanceof DataAccessResourceFailureException) {
			err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			err.setCode(RestErrorCode.DATASOURCE_FAILED.getValue());
			String m = getMessage(RestErrorCode.DATASOURCE_FAILED.getMessageKey(), null, 
					"Error connecting to database",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {ex.getMessage()});
			return;
		}
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(RestErrorCode.DATAACCESS_FAILED.getValue());
		String m = getMessage(RestErrorCode.DATAACCESS_FAILED.getMessageKey(), null, 
				"Error data access exception",locale);
		err.setMessage(m);
		err.setDetailedMessages(new String[] {ex.getMessage()});
		return;
	}
	
	private void resolveIOException(IOException ex, RestError err, Locale locale) {
		// Comprobar error Json
		if (ex instanceof JsonParseException || ex instanceof JsonMappingException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(RestErrorCode.INVALID_ARGUMENT.getValue());
			String m = getMessage(RestErrorCode.INVALID_ARGUMENT.getMessageKey(), null, 
					"Illegal arguments on request",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {ex.getMessage()});
			return;
		}
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(RestErrorCode.GENERIC_IOEXCEPTION.getValue());
		String m = getMessage(RestErrorCode.GENERIC_IOEXCEPTION.getMessageKey(), null, 
				"Server error in response generation.",locale);
		err.setMessage(m);
		err.setDetailedMessages(new String[] {ex.getMessage()});
	}
	
	private void resolveTransactionException(TransactionException ex, RestError err, Locale locale) {
		// No se puede crear la transacción. Posible perdida de conectividad a la BD
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(RestErrorCode.INTERNAL_SERVER_ERROR.getValue());
		Throwable ex1 = ex.getCause()==null? ex : ex.getCause();
		String m;
		if (ex1 instanceof PersistenceException) {
			ex1 = ex1.getCause()==null? ex : ex1.getCause();
			if (ex1 instanceof JDBCException) {
				err.setCode(RestErrorCode.DATASOURCE_CONNECTIONFAILED.getValue());
				m = getMessage(RestErrorCode.DATASOURCE_CONNECTIONFAILED.getMessageKey(), null, 
						"Error connecting to database",locale);
			} else {
				err.setCode(RestErrorCode.DATAACCESS_PERSISTENCEFAILED.getValue());
				m = getMessage(RestErrorCode.DATAACCESS_PERSISTENCEFAILED.getMessageKey(), null, 
						"Error accesing persistence layer",locale);
			}
		} else {
			err.setCode(RestErrorCode.DATAACCESS_TRANSACTIONFAILED.getValue());
			m = getMessage(RestErrorCode.DATAACCESS_TRANSACTIONFAILED.getMessageKey(), null, 
					"Error instantiating required transaction",locale);
		}
		err.setExceptionClass(ex1.getClass().getSimpleName());
		err.setMessage(m);
		err.setDetailedMessages(new String[] {ex1.getMessage()});
		err.setDebugMessages(getDebugMessage(ex1));
	}
	
	private void resolveUnhandledException(Throwable ex, RestError err,Locale locale) {
		err.setCode(RestErrorCode.UNKNOW.getValue());
		Throwable ex1 = ex.getCause()==null? ex : ex.getCause();
		String m = getMessage(RestErrorCode.UNKNOW.getMessageKey(), null, 
								ex.getMessage(),locale);
		err.setExceptionClass(ex1.getClass().getSimpleName());
		err.setMessage(m);
		err.setDetailedMessages(new String[] {ex1.getMessage()});
		err.setDebugMessages(getDebugMessage(ex1));
	}

	private class RestErrorTemplate extends RestError {
		public RestErrorTemplate() {
			super();
			setMoreInfoUrl("info@veisite.com");
		}
	}

}
