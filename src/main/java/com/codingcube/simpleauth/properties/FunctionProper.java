package com.codingcube.simpleauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simple-auth.func")
public class FunctionProper {
    private boolean dynamicAuth = false;
    private boolean autoClearExpiredRecord = false;
    private boolean dynamicLimit = false;

    private boolean handlerCache = true;
    private static boolean handlerCacheStatic = true;

    public boolean isHandlerCache() {
        return handlerCache;
    }

    public void setHandlerCache(boolean handlerCache) {
        this.handlerCache = handlerCache;
    }

    public static boolean isHandlerCacheStatic() {
        return handlerCacheStatic;
    }

    public static void setHandlerCacheStatic(boolean handlerCacheStatic) {
        FunctionProper.handlerCacheStatic = handlerCacheStatic;
    }

    public boolean isDynamicAuth() {
        return dynamicAuth;
    }

    public void setDynamicAuth(boolean dynamicAuth) {
        this.dynamicAuth = dynamicAuth;
    }

    public boolean isAutoClearExpiredRecord() {
        return autoClearExpiredRecord;
    }

    public void setAutoClearExpiredRecord(boolean autoClearExpiredRecord) {
        this.autoClearExpiredRecord = autoClearExpiredRecord;
    }

    public boolean isDynamicLimit() {
        return dynamicLimit;
    }

    public void setDynamicLimit(boolean dynamicLimit) {
        this.dynamicLimit = dynamicLimit;
    }
}
