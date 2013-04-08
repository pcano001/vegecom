package com.veisite.vegecom.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.PersistenceException;

import org.hibernate.exception.JDBCConnectionException;
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
import com.veisite.vegecom.rest.error.RestErrorResolver;

public class DefaultRestErrorResolver implements RestErrorResolver, MessageSourceAware {
	
	/**
	 * claves de mensajes para la resolucion de mensajes de error i18n
	 */
	private static final String UNKNOWN_ERROR="com.veisite.vegecom.rest.error.UnknownError";
	private static final String ILLEGALARGUMENT_ERROR="com.veisite.vegecom.rest.error.IllegalArgumentError";
	private static final String IOEXCEPTION_ERROR="com.veisite.vegecom.rest.error.IOExceptionError";
	private static final String RESOURCENOTFOUND_ERROR="com.veisite.vegecom.rest.error.ResourceNotFoundError";
	private static final String DATACONCURRENCY_ERROR="com.veisite.vegecom.rest.error.DataConcurrencyError";
	private static final String INTEGRITYVIOLATION_ERROR="com.veisite.vegecom.rest.error.IntegrityViolationError";
	private static final String DATABASEACCESS_ERROR="com.veisite.vegecom.rest.error.DatabaseAccessError";
	private static final String GENERICDATAACCESS_ERROR="com.veisite.vegecom.rest.error.GenericDataAccessError";
	private static final String PERSISTENCELEVEL_ERROR="com.veisite.vegecom.rest.error.PersistenceLevelError";
	private static final String TRANSACTION_ERROR="com.veisite.vegecom.rest.error.TransactionError";
	
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
	public RestError resolve(Exception ex) {
		RestError err;
		if (ex instanceof RestException) {
			err = resolveRestException((RestException) ex);
			if (err.getExceptionClass()==null) {
				if (ex.getCause()!=null) err.setExceptionClass(ex.getCause().getClass().getSimpleName());
				else err.setExceptionClass(ex.getClass().getSimpleName());
			}
		} else {
			err = unknownError(ex);
			if (err.getExceptionClass()==null) err.setExceptionClass(ex.getClass().getSimpleName());
		}
		if (err.getDebugMessages()==null) err.setDebugMessages(getDebugMessage(ex));
		return err;
	}
	
	private String getMessage(String code, Object [] args, String defaultMessage) {
		if (messageSource==null) return defaultMessage;
		return messageSource.getMessage(code, args, defaultMessage, Locale.getDefault());
	}
	
	private RestError unknownError(Exception ex) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(ex,err);
		if (err.getStatus()==null) err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		if (err.getCode()==0) err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		if (err.getMessage()==null) {
			String m = getMessage(UNKNOWN_ERROR,null,"Unexpected error");
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
	
	private RestError resolveRestException(RestException re) {
		if (re instanceof RestClientException)
			return resolveRestClientException((RestClientException) re);
		if (re instanceof RestServerException)
			return resolveRestServerException((RestServerException) re);
		return null;
	}
	
	private RestError resolveRestClientException(RestClientException rce) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(rce.getCause(), err);
		if (err.getCode() == 0) err.setCode(HttpStatus.BAD_REQUEST.value());
		return err;
	}

	private RestError resolveRestServerException(RestServerException rce) {
		RestError err = new RestErrorTemplate();
		resolveInternalException(rce.getCause(), err);
		if (err.getCode() == 0) err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return err;
	}
	
	private void resolveInternalException(Throwable ex, RestError err) {
		if (ex==null) return;
		// Comprobemos si es una excepcion de acceso a datos
		if (ex instanceof DataAccessException) resolveDataAccessException((DataAccessException)ex,err);
		if (ex instanceof TransactionException) resolveTransactionException((TransactionException)ex,err);
		if (ex instanceof IOException) resolveIOException((IOException)ex,err);
	}

	private void resolveDataAccessException(DataAccessException ex, RestError err) {
		if (ex instanceof DataRetrievalFailureException) {
			err.setStatus(HttpStatus.NOT_FOUND);
			err.setCode(HttpStatus.NOT_FOUND.value());
			String m = getMessage(RESOURCENOTFOUND_ERROR, null, "Resource not found");
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof InvalidDataAccessApiUsageException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(HttpStatus.BAD_REQUEST.value());
			String m = getMessage(ILLEGALARGUMENT_ERROR, null, "Illegal arguments on request");
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof ConcurrencyFailureException) {
			err.setStatus(HttpStatus.CONFLICT);
			err.setCode(HttpStatus.CONFLICT.value());
			String m = getMessage(DATACONCURRENCY_ERROR, null, "Update Conflict. Data may has been updated by another session");
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof DataIntegrityViolationException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(HttpStatus.BAD_REQUEST.value());
			String m = getMessage(INTEGRITYVIOLATION_ERROR, null, "Data integrity violation");
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		if (ex instanceof DataAccessResourceFailureException) {
			err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			String m = getMessage(DATABASEACCESS_ERROR, null, "Error connecting to database");
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String m = getMessage(GENERICDATAACCESS_ERROR, null, "Error data access exception");
		err.setMessage(m);
		err.setDetailedMessages(new String[] {m,ex.getMessage()});
		return;
	}
	
	private void resolveIOException(IOException ex, RestError err) {
		// Comprobar error Json
		if (ex instanceof JsonParseException || ex instanceof JsonMappingException) {
			err.setStatus(HttpStatus.BAD_REQUEST);
			err.setCode(HttpStatus.BAD_REQUEST.value());
			String m = getMessage(ILLEGALARGUMENT_ERROR, null, "Illegal arguments on request");
			err.setMessage(m);
			err.setDetailedMessages(new String[] {m,ex.getMessage()});
			return;
		}
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		String m = getMessage(IOEXCEPTION_ERROR, null, "Server error generating response");
		err.setMessage(m);
		err.setDetailedMessages(new String[] {m,ex.getMessage()});
	}
	
	private void resolveTransactionException(TransactionException ex, RestError err) {
		// No se puede crear la transacción. Posible perdida de conectividad a la BD
		err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		err.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		Throwable ex1 = ex.getCause()==null? ex : ex.getCause();
		String m;
		if (ex1 instanceof PersistenceException) {
			ex1 = ex1.getCause()==null? ex : ex1.getCause();
			if (ex1 instanceof JDBCConnectionException) 
				m = getMessage(DATABASEACCESS_ERROR, null, "Error connecting to database");
			else 
				m = getMessage(PERSISTENCELEVEL_ERROR, null, "Error accesing persistence layer");
		} else m = getMessage(TRANSACTION_ERROR, null, "Error instantiating required transaction");
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
