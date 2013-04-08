package com.veisite.vegecom.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

public class RestError {

    private HttpStatus status;
    private int code;
    private String exceptionClass;
    private String message;
    private String[] detailedMessages;
    private String[] debugMessages;
    private String moreInfoUrl;

    public RestError() {
    }
    
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getDetailedMessages() {
		return detailedMessages;
	}

	public void setDetailedMessages(String[] detailedMessages) {
		this.detailedMessages = detailedMessages;
	}

	public String[] getDebugMessages() {
		return debugMessages;
	}

	public void setDebugMessages(String[] debugMessages) {
		this.debugMessages = debugMessages;
	}

	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}

	public void setMoreInfoUrl(String moreInfoUrl) {
		this.moreInfoUrl = moreInfoUrl;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RestError) {
            RestError re = (RestError) o;
            return ObjectUtils.nullSafeEquals(getStatus(), re.getStatus()) &&
                    getCode() == re.getCode() &&
                    ObjectUtils.nullSafeEquals(getMessage(), re.getMessage()) &&
                    ObjectUtils.nullSafeEquals(getDetailedMessages(), re.getDetailedMessages()) &&
                    ObjectUtils.nullSafeEquals(getDebugMessages(), re.getDebugMessages()) &&
                    ObjectUtils.nullSafeEquals(getMoreInfoUrl(), re.getMoreInfoUrl());
        }
        return false;
    }

    @Override
    public int hashCode() {
        //noinspection ThrowableResultOfMethodCallIgnored
        return ObjectUtils.nullSafeHashCode(new Object[]{
                getStatus(), getCode(), getMessage(), getDetailedMessages(), getDebugMessages(), getMoreInfoUrl()
        });
    }

    public String toString() {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder().append(getStatus().value())
                .append(" (").append(getStatus().getReasonPhrase()).append(" )")
                .toString();
    }

}
