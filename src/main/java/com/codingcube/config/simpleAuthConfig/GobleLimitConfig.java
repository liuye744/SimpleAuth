package com.codingcube.config.simpleAuthConfig;

import com.codingcube.config.SimpleAuthWebConfig;
import com.codingcube.handler.simpleAuthHandler.GobleLimitHandler;


public class GobleLimitConfig extends SimpleAuthWebConfig {
    @Override
    public void addAuthHandlers() {
        addAuthHandler(GobleLimitHandler.class).addPathPatterns("/*").order(1);
    }
}
