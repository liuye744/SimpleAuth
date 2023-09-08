package com.codingcube.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.logging.Level;

@ConfigurationProperties(prefix = "simple-auth.log")
public class LogProper {
    private String logImpl = "NoLogging";

    public String getLogImpl() {
        return logImpl;
    }

    public void setLogImpl(String logImpl) {
        this.logImpl = logImpl;
    }
}
