package com.codingcube.exception;

public class AccessIsRestricted extends RuntimeException{
    public AccessIsRestricted(String message) {
        super(message);
    }

    public AccessIsRestricted() {
    }
}
