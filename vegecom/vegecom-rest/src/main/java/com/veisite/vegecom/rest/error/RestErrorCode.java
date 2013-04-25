package com.veisite.vegecom.rest.error;

public enum RestErrorCode {
	
	UNKNOW(50090,"error.rest.UnknownError"),
	INTERNAL_SERVER_ERROR(50010,"error.rest.InternalServerError"),
	DATASOURCE_FAILED(50020,"error.rest.DataSourceFailed"),
	DATASOURCE_CONNECTIONFAILED(50030,"error.rest.DataSourceConectionFailed"),
	DATAACCESS_FAILED(50040,"error.rest.DataAccessFailed"),
	DATAACCESS_TRANSACTIONFAILED(50045,"error.rest.DataAccessTransactionFailed"),
	DATAACCESS_PERSISTENCEFAILED(50045,"error.rest.DataAccessPersistenceFailed"),
	GENERIC_IOEXCEPTION(50080,"error.rest.GenericIOException"),
	BAD_REQUEST(40090,"error.rest.BadRequest"),
	PATH_NOT_FOUND(40005,"error.rest.PathNotFound"),
	RESOURCE_NOT_FOUND(40010,"error.rest.ResourceNotFound"),
	INVALID_ARGUMENT(40020,"error.rest.IllegalArgument"),
	DATACONCURRENCY_CONFLICT(40030,"error.rest.DataConcurrencyConflict"),
	DATAINTEGRITY_VIOLATION(40040,"error.rest.DataIntegrityViolation"),
	UNAUTHENTICATED_REQUEST(60010,"error.rest.UnAuthenticatedRequest"),
	SESSION_INVALID(60020,"error.rest.SessionInvalid"),
	SESSION_EXPIRED(60030,"error.rest.SessionExpired"),
	UNAUTHORIZED_REQUEST(60040,"error.rest.UnAuthorizatedRequest")
	;
	
	private int value;
	
	private String messageKey;
	
	RestErrorCode(int value, String messageKey) {
		this.value = value;
		this.messageKey = messageKey;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getMessageKey() {
		return messageKey;
	}
	
	public static RestErrorCode getErrorCode(int value) {
		for (RestErrorCode ec : RestErrorCode.values()) {
			if (ec.value==value) return ec;
		}
		return null;
	}

}
