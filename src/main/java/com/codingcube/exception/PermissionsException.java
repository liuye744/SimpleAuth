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
}
