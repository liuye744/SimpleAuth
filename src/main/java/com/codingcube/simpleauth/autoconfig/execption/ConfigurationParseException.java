package com.codingcube.simpleauth.autoconfig.execption;

public class ConfigurationParseException extends RuntimeException{
    public ConfigurationParseException() {
    }

    public ConfigurationParseException(String message) {
        super(message);
    }

    public ConfigurationParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationParseException(Throwable cause) {
        super(cause);
    }

    public ConfigurationParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
