package com.codingcube.exception;

public class AccessIsRestrictedException extends RuntimeException{
    public AccessIsRestrictedException(String message) {
        super(message);
    }

    public AccessIsRestrictedException() {
    }
}
