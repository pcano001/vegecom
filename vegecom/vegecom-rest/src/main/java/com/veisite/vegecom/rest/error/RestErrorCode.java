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
	RESOURCE_NOT_FOUND(40010,"error.rest.ResourceNotFound"),
	INVALID_ARGUMENT(40020,"error.rest.IllegalArgument"),
	DATACONCURRENCY_CONFLICT(40030,"error.rest.DataConcurrencyConflict"),
	DATAINTEGRITY_VIOLATION(40040,"error.rest.DataIntegrityViolation")
	;
	
	private int value;
	
	private String messageKey;
	
	RestErrorCode(int value, String messageKey) {
		this.value = value;
		this.messageKey = messageKey;
	}
	
	public int value() {
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
