package com.codingcube.exception;

/**
 * @author dhc
 */
public class PermissionsException extends RuntimeException{

    public PermissionsException() {
    }

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionsException(Throwable cause) {
        super(cause);
    }

    public PermissionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
