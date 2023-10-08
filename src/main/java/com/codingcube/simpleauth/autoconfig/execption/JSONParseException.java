package com.codingcube.simpleauth.autoconfig.execption;

public class JSONParseException extends RuntimeException{

    public JSONParseException() {
    }

    public JSONParseException(String message) {
        super(message);
    }

    public JSONParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONParseException(Throwable cause) {
        super(cause);
    }

    public JSONParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
