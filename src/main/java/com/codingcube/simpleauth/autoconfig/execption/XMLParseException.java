package com.codingcube.simpleauth.autoconfig.execption;

public class XMLParseException extends RuntimeException{

    public XMLParseException() {
    }

    public XMLParseException(String message) {
        super(message);
    }

    public XMLParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLParseException(Throwable cause) {
        super(cause);
    }

    public XMLParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
