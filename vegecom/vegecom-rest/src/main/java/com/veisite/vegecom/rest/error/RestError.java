package com.veisite.vegecom.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

public class RestError {

    private final HttpStatus status;
    private final int code;
    private final String message;
    private final String htmlMessage;
    private final String debugHtmlMessage;
    private final String moreInfoUrl;

    public RestError(HttpStatus status, int code, String message, String htmlMessage, 
    		String debugHtmlMessage, String moreInfoUrl) {
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.htmlMessage = htmlMessage;
        this.debugHtmlMessage = debugHtmlMessage;
        this.moreInfoUrl = moreInfoUrl;
    }


    public HttpStatus getStatus() {
		return status;
	}


	public int getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}


	public String getHtmlMessage() {
		return htmlMessage;
	}


	public String getDebugHtmlMessage() {
		return debugHtmlMessage;
	}


	public String getMoreInfoUrl() {
		return moreInfoUrl;
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
                    ObjectUtils.nullSafeEquals(getHtmlMessage(), re.getHtmlMessage()) &&
                    ObjectUtils.nullSafeEquals(getDebugHtmlMessage(), re.getDebugHtmlMessage()) &&
                    ObjectUtils.nullSafeEquals(getMoreInfoUrl(), re.getMoreInfoUrl());
        }
        return false;
    }

    @Override
    public int hashCode() {
        //noinspection ThrowableResultOfMethodCallIgnored
        return ObjectUtils.nullSafeHashCode(new Object[]{
                getStatus(), getCode(), getMessage(), getHtmlMessage(), getDebugHtmlMessage(), getMoreInfoUrl()
        });
    }

    public String toString() {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder().append(getStatus().value())
                .append(" (").append(getStatus().getReasonPhrase()).append(" )")
                .toString();
    }

}
