package com.codingcube.simpleauth.properties;

import com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simple-auth.limit")
public class LimitProper {

    private String defaultRejected = "com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem";
    private static Class<? extends RejectedStratagem> defaultRejectedClazz = DefaultLimitRejectedStratagem.class;

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
}
