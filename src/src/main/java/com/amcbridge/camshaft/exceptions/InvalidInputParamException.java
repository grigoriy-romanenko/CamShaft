package com.amcbridge.camshaft.exceptions;

public class InvalidInputParamException extends RuntimeException {

    public InvalidInputParamException() {
        super();
    }

    public InvalidInputParamException(String message) {
        super(message);
    }

    public InvalidInputParamException(Throwable cause) {
        super(cause);
    }

    public InvalidInputParamException(String message, Throwable cause) {
        super(message, cause);
    }

}