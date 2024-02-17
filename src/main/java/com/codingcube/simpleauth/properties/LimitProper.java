package com.codingcube.simpleauth.properties;

import com.codingcube.simpleauth.limit.cache.KeyCache;
import com.codingcube.simpleauth.limit.cache.impl.DefaultCache;
import com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

@ConfigurationProperties(prefix = "simple-auth.limit")
public class LimitProper {

    private String defaultRejected = "com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem";
    private static Class<? extends RejectedStratagem> defaultRejectedClazz = DefaultLimitRejectedStratagem.class;

    private String defaultBanCache = "com.codingcube.simpleauth.limit.cache.impl.DefaultCache";
    private static Class<? extends KeyCache> defaultBanCacheClazz = DefaultCache.class;

    private String defaultLimitCache = "com.codingcube.simpleauth.limit.cache.impl.DefaultCache";
    private static Class<? extends KeyCache> defaultLimitCacheClazz = DefaultCache.class;

    private String defaultSecondsCache = "com.codingcube.simpleauth.limit.cache.impl.DefaultCache";
    private static Class<? extends KeyCache> defaultSecondsCacheClazz = DefaultCache.class;



    public String getDefaultRejected() {
        return defaultRejected;
    }

    public void setDefaultRejected(String defaultRejected) {
        this.defaultRejected = defaultRejected;

    }

    public static Class<? extends RejectedStratagem> getDefaultRejectedClazz() {
        return defaultRejectedClazz;
    }

    public static void setDefaultRejectedClazz(Class<? extends RejectedStratagem> defaultRejectedClazz) {
        LimitProper.defaultRejectedClazz = defaultRejectedClazz;
    }

    public String getDefaultBanCache() {
        return defaultBanCache;
    }

    public void setDefaultBanCache(String defaultBanCache) {
        this.defaultBanCache = defaultBanCache;
    }

    public static Class<? extends KeyCache> getDefaultBanCacheClazz() {
        return defaultBanCacheClazz;
    }

    public static void setDefaultBanCacheClazz(Class<? extends KeyCache> defaultBanCacheClazz) {
        LimitProper.defaultBanCacheClazz = defaultBanCacheClazz;
    }

    public String getDefaultLimitCache() {
        return defaultLimitCache;
    }

    public void setDefaultLimitCache(String defaultLimitCache) {
        this.defaultLimitCache = defaultLimitCache;
    }

    public static Class<? extends KeyCache> getDefaultLimitCacheClazz() {
        return defaultLimitCacheClazz;
    }

    public static void setDefaultLimitCacheClazz(Class<? extends KeyCache> defaultLimitCacheClazz) {
        LimitProper.defaultLimitCacheClazz = defaultLimitCacheClazz;
    }

    public String getDefaultSecondsCache() {
        return defaultSecondsCache;
    }

    public void setDefaultSecondsCache(String defaultSecondsCache) {
        this.defaultSecondsCache = defaultSecondsCache;
    }

    public static Class<? extends KeyCache> getDefaultSecondsCacheClazz() {
        return defaultSecondsCacheClazz;
    }

    public static void setDefaultSecondsCacheClazz(Class<? extends KeyCache> defaultSecondsCacheClazz) {
        LimitProper.defaultSecondsCacheClazz = defaultSecondsCacheClazz;
    }
}
