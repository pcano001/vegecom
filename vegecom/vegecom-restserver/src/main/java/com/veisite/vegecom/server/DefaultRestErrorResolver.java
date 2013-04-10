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
import com.veisite.vegecom.rest.RestClientException;
import com.veisite.vegecom.rest.RestException;
import com.veisite.vegecom.rest.RestServerException;
import com.veisite.vegecom.rest.error.RestError;
import com.veisite.vegecom.rest.error.RestErrorCode;
import com.veisite.vegecom.rest.error.RestErrorResolver;

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
		if (err.getCode()==0) err.setCode(RestErrorCode.UNKNOW.value());
		if (err.getMessage()==null) {
			String m = getMessage(RestErrorCode.UNKNOW.getMessageKey(),null,"Unexpected error", locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m});
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
		if (re instanceof RestClientException)
			return resolveRestClientException((RestClientException) re, locale);
		if (re instanceof RestServerException)
			return resolveRestServerException((RestServerException) re, locale);
		return null;
	}
	
	private RestError resolveRestClientException(RestClientException rce, Locale locale) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(rce.getCause(), err, locale);
		if (err.getCode() == 0) err.setCode(RestErrorCode.BAD_REQUEST.value());
		return err;
	}

	private RestError resolveRestServerException(RestServerException rce, Locale locale) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(rce.getCause(), err, locale);
		if (err.getCode() == 0) err.setCode(RestErrorCode.INTERNAL_SERVER_ERROR.value());
		return err;
	}
	
	private void resolveInternalException(Throwable ex, RestError err, Locale locale) {
		if (ex==null) return;
		// Comprobemos si es una excepcion de acceso a datos
		if (ex instanceof DataAccessException) 
			resolveDataAccessException((DataAccessException)ex,err,locale);
		if (ex instanceof TransactionException) 
			resolveTransactionException((TransactionException)ex,err,locale);
		if (ex instanceof IOException) 
			resolveIOException((IOException)ex,err,locale);
	}

	private void resolveDataAccessException(DataAccessException ex, RestError err, Locale locale) {
		if (ex instanceof DataRetrievalFailureException) {
			err.setStatus(HttpStatus.NOT_FOUND);
			err.setCode(RestErrorCode.RESOURCE_NOT_FOUND.value());
			String m = getMessage(RestErrorCode.RESOURCE_NOT_FOUND.getMessageKey(), null, 
					"Resource not found", locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof InvalidDataAccessApiUsageException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(RestErrorCode.INVALID_ARGUMENT.value());
			String m = getMessage(RestErrorCode.INVALID_ARGUMENT.getMessageKey(), null, 
					"Illegal arguments on request",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof ConcurrencyFailureException) {
			err.setStatus(HttpStatus.CONFLICT);
			err.setCode(RestErrorCode.DATACONCURRENCY_CONFLICT.value());
			String m = getMessage(RestErrorCode.DATACONCURRENCY_CONFLICT.getMessageKey(), null, 
					"Update Conflict. It seems that data has been updated or deleted by another session",
					locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof DataIntegrityViolationException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(RestErrorCode.DATAINTEGRITY_VIOLATION.value());
			String m = getMessage(RestErrorCode.DATAINTEGRITY_VIOLATION.getMessageKey(), null, 
					"Data integrity violation",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof DataAccessResourceFailureException) {
			err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			err.setCode(RestErrorCode.DATASOURCE_FAILED.value());
			String m = getMessage(RestErrorCode.DATASOURCE_FAILED.getMessageKey(), null, 
					"Error connecting to database",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(RestErrorCode.DATAACCESS_FAILED.value());
		String m = getMessage(RestErrorCode.DATAACCESS_FAILED.getMessageKey(), null, 
				"Error data access exception",locale);
		err.setMessage(m);
		err.setDetailedMessages(new String[] {m,ex.getMessage()});
		return;
	}
	
	private void resolveIOException(IOException ex, RestError err, Locale locale) {
		// Comprobar error Json
		if (ex instanceof JsonParseException || ex instanceof JsonMappingException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(RestErrorCode.INVALID_ARGUMENT.value());
			String m = getMessage(RestErrorCode.INVALID_ARGUMENT.getMessageKey(), null, 
					"Illegal arguments on request",locale);
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(RestErrorCode.GENERIC_IOEXCEPTION.value());
		String m = getMessage(RestErrorCode.GENERIC_IOEXCEPTION.getMessageKey(), null, 
				"Server error in response generation.",locale);
		err.setMessage(m);
		err.setDetailedMessages(new String[] {m,ex.getMessage()});
	}
	
	private void resolveTransactionException(TransactionException ex, RestError err, Locale locale) {
		// No se puede crear la transacción. Posible perdida de conectividad a la BD
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(RestErrorCode.INTERNAL_SERVER_ERROR.value());
		Throwable ex1 = ex.getCause()==null? ex : ex.getCause();
		String m;
		if (ex1 instanceof PersistenceException) {
			ex1 = ex1.getCause()==null? ex : ex1.getCause();
			if (ex1 instanceof JDBCException) {
				err.setCode(RestErrorCode.DATASOURCE_CONNECTIONFAILED.value());
				m = getMessage(RestErrorCode.DATASOURCE_CONNECTIONFAILED.getMessageKey(), null, 
						"Error connecting to database",locale);
			} else {
				err.setCode(RestErrorCode.DATAACCESS_PERSISTENCEFAILED.value());
				m = getMessage(RestErrorCode.DATAACCESS_PERSISTENCEFAILED.getMessageKey(), null, 
						"Error accesing persistence layer",locale);
			}
		} else {
			err.setCode(RestErrorCode.DATAACCESS_TRANSACTIONFAILED.value());
			m = getMessage(RestErrorCode.DATAACCESS_TRANSACTIONFAILED.getMessageKey(), null, 
					"Error instantiating required transaction",locale);
		}
		err.setExceptionClass(ex1.getClass().getSimpleName());
		err.setMessage(m);
		err.setDetailedMessages(new String[] {m,ex1.getMessage()});
		err.setDebugMessages(getDebugMessage(ex1));
	}
	
	
	
	private class RestErrorTemplate extends RestError {
		public RestErrorTemplate() {
			super();
			setMoreInfoUrl("info@veisite.com");
		}
	}
	
}
