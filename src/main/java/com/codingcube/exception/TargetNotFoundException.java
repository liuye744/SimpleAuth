package com.codingcube.exception;

public class TargetNotFoundException extends RuntimeException {
    public TargetNotFoundException() {
    }

    public TargetNotFoundException(String message) {
        super(message);
    }

    public TargetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TargetNotFoundException(Throwable cause) {
        super(cause);
    }

    public TargetNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
