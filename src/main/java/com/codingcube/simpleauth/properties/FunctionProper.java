package com.codingcube.simpleauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "simple-auth.func")
public class FunctionProper {
    private boolean dynamicAuth = false;
    private boolean autoClearExpiredRecord = false;
    private boolean dynamicLimit = false;

    private boolean handlerCache = true;
    private static boolean handlerCacheStatic = true;

    private List<String> clickjackingPath = new ArrayList<>();
    private String XFrameOptions = "SAMEORIGIN";

    public String getXFrameOptions() {
        return XFrameOptions;
    }

    public void setXFrameOptions(String XFrameOptions) {
        this.XFrameOptions = XFrameOptions;
    }

    public List<String> getClickjackingPath() {
        return clickjackingPath;
    }

    public void setClickjackingPath(List<String> clickjackingPath) {
        this.clickjackingPath = clickjackingPath;
    }

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
