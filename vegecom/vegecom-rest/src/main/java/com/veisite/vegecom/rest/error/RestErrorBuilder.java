package com.veisite.vegecom.rest.error;

import org.springframework.http.HttpStatus;

public class RestErrorBuilder {

    private HttpStatus status;
    private int code;
    private String message;
    private String developerMessage;
    private String moreInfoUrl;

    public RestErrorBuilder() {
    }

    public RestErrorBuilder setStatus(int statusCode) {
        this.status = HttpStatus.valueOf(statusCode);
        return this;
    }

    public RestErrorBuilder setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public RestErrorBuilder setCode(int code) {
        this.code = code;
        return this;
    }

    public RestErrorBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public RestErrorBuilder setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
        return this;
    }

    public RestErrorBuilder setMoreInfoUrl(String moreInfoUrl) {
        this.moreInfoUrl = moreInfoUrl;
        return this;
    }

    public RestError build() {
        if (this.status == null) {
            this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new RestError(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl);
    }

}
