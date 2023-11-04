package com.codingcube.simpleauth.exception;

public class ValidateMethodException extends RuntimeException{

    public ValidateMethodException() {
    }

    public ValidateMethodException(String message) {
        super(message);
    }

    public ValidateMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateMethodException(Throwable cause) {
        super(cause);
    }

    public ValidateMethodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
