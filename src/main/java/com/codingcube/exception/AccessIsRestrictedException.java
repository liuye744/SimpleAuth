package com.codingcube.exception;

public class AccessIsRestrictedException extends RuntimeException{
    public AccessIsRestrictedException(String message) {
        super(message);
    }

    public AccessIsRestrictedException() {
        super("Access is restricted.Is there an ExceptionHandler for AccessIsRestricted");
    }
}
