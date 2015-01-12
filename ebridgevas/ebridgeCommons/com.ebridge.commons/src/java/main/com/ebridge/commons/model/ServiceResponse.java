package com.ebridge.commons.model;

/**
 * @author david@tekeshe.com
 */
public class ServiceResponse {

    private final String message;

    public ServiceResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
