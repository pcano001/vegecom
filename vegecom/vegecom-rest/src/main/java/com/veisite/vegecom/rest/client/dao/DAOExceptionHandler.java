package com.veisite.vegecom.rest.client.dao;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import com.veisite.vegecom.rest.error.RestError;
import com.veisite.vegecom.rest.error.RestErrorCode;
import com.veisite.vegecom.service.SerializationException;
import com.veisite.vegecom.service.SerializationService;

public class DAOExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String RESOURCEACCESSEXCEPTION_KEY = "error.rest.dao.ResourceAccessException";
	private static final String UNKNOWNDATAACCESSEXCEPTION_KEY = "error.rest.dao.UnknownDataAccessException";
	private static final String UNEXPECTEDCLIENTRESPONSEFORMAT_KEY = "error.rest.dao.UnexpectedResponseFormatClientException";
	private static final String UNEXPECTEDSERVERRESPONSEFORMAT_KEY = "error.rest.dao.UnexpectedResponseFormatServerException";
	private static final String SERVERERROREXCEPTION_KEY = "error.rest.dao.ServerErrorExceptionMessage";
	private static final String CLIENTERROREXCEPTION_KEY = "error.rest.dao.ClientErrorExceptionMessage";
	private static final String SIMPLESERVERERROREXCEPTION_KEY = "error.rest.dao.SimpleServerErrorException";
	private static final String SIMPLECLIENTERROREXCEPTION_KEY = "error.rest.dao.SimpleClientErrorException";
	
	private SerializationService serializationService;

	private MessageSource messageSource = null;
	
	public DataAccessException getDataAccessException(RestClientException exception) {
		logger.debug("Converting a {} to DataAccessException.",exception.getClass().getName());
		String message;
		// Ver si se trata de un error del lado de la petición en el cliente.
		if (exception instanceof ResourceAccessException) {
			message = "Error conecting to server";
			if (messageSource!=null)
				message = messageSource.getMessage(RESOURCEACCESSEXCEPTION_KEY, null, message, Locale.getDefault());
			return new DataAccessResourceFailureException(message,exception);
		}
		if (exception instanceof HttpStatusCodeException) {
			HttpStatusCodeException e = (HttpStatusCodeException) exception; 
			logger.debug("Response Header: {}",e.getResponseHeaders().toString());
			logger.debug("Response Body: \n{}",e.getResponseBodyAsString());
			return getDataAccessFromHttpError((HttpStatusCodeException) exception);
		}
		message = "Error requesting to server"; 
		if (messageSource!=null)
			message = messageSource.getMessage(UNKNOWNDATAACCESSEXCEPTION_KEY, null, message, Locale.getDefault());
		return new DataAccessResourceFailureException(message, exception);
	}

	/**
	 * Metodo que maneja los errores que se envían desde el servidor.
	 * 
	 * @param exception
	 * @return
	 */
	private DataAccessException getDataAccessFromHttpError(HttpStatusCodeException exception) {
		String message;
		// Si la respuesta enviada por el servidor no es del tipo
		// application/json entonces se ha producido un error de sintaxis en la
		// propia solicitud. Error en el uso de la API.
		HttpHeaders headers = exception.getResponseHeaders();
		if (headers.getContentType()==null ||
				!headers.getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)) {
			logger.debug("Content-type not {}: {}",MediaType.APPLICATION_JSON.toString(),headers.getContentType().toString());
			if (exception instanceof HttpClientErrorException) {
				message = "Internal error. Incorret request path to server or incorrect server api use.";
				if (messageSource!=null)
					message = messageSource.getMessage(UNEXPECTEDCLIENTRESPONSEFORMAT_KEY, 
							null, message, Locale.getDefault());
			} else {
				message = "Internal error. Server reports an error with unexpected format.";
				if (messageSource!=null)
					message = messageSource.getMessage(UNEXPECTEDSERVERRESPONSEFORMAT_KEY, 
							null, message, Locale.getDefault());
			}
			return new InvalidDataAccessApiUsageException(message,exception);
		}
		
		// Obtenemos el objeto de error de la respuesta del servidor
		RestError restError = null;
		String serror = exception.getResponseBodyAsString();
		try {
			restError = serializationService.read(serror, RestError.class);
		} catch (SerializationException se) {}
		
		// Si no podemos tener el objeto de error, devolvemos una excepcion genérica.
		if (restError==null) getDataAccessFromHttpStatus(exception);
		return getDataAccessFromRestError(restError, exception);
	}
	
	private DataAccessException getDataAccessFromRestError(RestError restError, HttpStatusCodeException exception) {
		// Incluir la información de detalle como excepciones encadenadas
		HelperRestErrorException cause = null;
		String[] detail = restError.getDetailedMessages();
		for (int i=detail.length-1; i>=0; i--) {
			if (cause==null || !detail[i].equals(cause.getMessage())) {
				HelperRestErrorException e = new HelperRestErrorException(detail[i], cause);
				cause = e;
			}
		}
		RestErrorCode code = RestErrorCode.getErrorCode(restError.getCode());
		String message = "";
		if (exception.getStatusCode().series()==HttpStatus.Series.SERVER_ERROR) {
			message = "The server has reported an internal error";
			message = messageSource.getMessage(SERVERERROREXCEPTION_KEY, 
					null, message, Locale.getDefault());
			message += "\n";
		}
		if (exception.getStatusCode().series()==HttpStatus.Series.SERVER_ERROR) {
			message = "The server has reported an error processing the request";
			message = messageSource.getMessage(CLIENTERROREXCEPTION_KEY, 
					null, message, Locale.getDefault());
			message += "\n";
		}
		message += restError.getMessage();
		switch (code) {
		case UNKNOW:
		case INTERNAL_SERVER_ERROR:
		case DATASOURCE_FAILED:
		case DATASOURCE_CONNECTIONFAILED:
		case DATAACCESS_FAILED:
		case DATAACCESS_TRANSACTIONFAILED:
		case DATAACCESS_PERSISTENCEFAILED:
		case GENERIC_IOEXCEPTION:
			return new DataAccessResourceFailureException(message,cause);
		case BAD_REQUEST:
			return new InvalidDataAccessResourceUsageException(message,cause);
		case RESOURCE_NOT_FOUND:
			return new DataRetrievalFailureException(message,cause);
		case INVALID_ARGUMENT:
			return new InvalidDataAccessApiUsageException(message,cause);
		case DATACONCURRENCY_CONFLICT:
			return new ConcurrencyFailureException(message,cause);
		case DATAINTEGRITY_VIOLATION:
			return new DataIntegrityViolationException(message,cause);
		default:
			return new DataAccessResourceFailureException(message,cause);
		}
	}
	
	private DataAccessException getDataAccessFromHttpStatus(HttpStatusCodeException exception) {
		String message;
		HttpStatus code = exception.getStatusCode();
		if (code.series()==HttpStatus.Series.SERVER_ERROR) {
			message = "Server error. Status code {}-{}.";
			if (messageSource!=null)
				message = messageSource.getMessage(SIMPLESERVERERROREXCEPTION_KEY, 
					new Object[] {code.value(), code.getReasonPhrase()}, message, Locale.getDefault());
				return new UnexpectedRestErrorException(message,exception);
		}
		message = "Server reports an error in client request. Status code {}-{}.";
		if (messageSource!=null)
			message = messageSource.getMessage(SIMPLECLIENTERROREXCEPTION_KEY, 
				new Object[] {code.value(), code.getReasonPhrase()}, message, Locale.getDefault());
		return new UnexpectedRestErrorException(message,exception);
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the serializationService
	 */
	public SerializationService getSerializationService() {
		return serializationService;
	}

	/**
	 * @param serializationService the serializationService to set
	 */
	public void setSerializationService(SerializationService serializationService) {
		this.serializationService = serializationService;
	}
}
