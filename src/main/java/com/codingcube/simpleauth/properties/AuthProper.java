package com.codingcube.simpleauth.properties;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.DefaultAuthHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simple-auth.auth")
public class AuthProper {
    private String verifyKey = "simple_auth_verify_key";
    private static String verifyKeyStatic = "simple_auth_verify_key";
    private String verifyValue = "8552D986272D4ACFAD5933D76262212B";
    private static String verifyValueStatic = "8552D986272D4ACFAD5933D76262212B";

    private String defaultHandler = "com.codingcube.simpleauth.auth.handler.DefaultAuthHandler";
    private static Class<? extends AutoAuthHandler> defaultHandlerClazz = DefaultAuthHandler.class;

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public static String getVerifyKeyStatic() {
        return verifyKeyStatic;
    }

    public static void setVerifyKeyStatic(String verifyKeyStatic) {
        AuthProper.verifyKeyStatic = verifyKeyStatic;
    }

    public String getVerifyValue() {
        return verifyValue;
    }

    public void setVerifyValue(String verifyValue) {
        this.verifyValue = verifyValue;
    }

    public static String getVerifyValueStatic() {
        return verifyValueStatic;
    }

    public static void setVerifyValueStatic(String verifyValueStatic) {
        AuthProper.verifyValueStatic = verifyValueStatic;
    }

    public String getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(String defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public static Class<? extends AutoAuthHandler> getDefaultHandlerClazz() {
        return defaultHandlerClazz;
    }

    public static void setDefaultHandlerClazz(Class<? extends AutoAuthHandler> defaultHandlerClazz) {
        AuthProper.defaultHandlerClazz = defaultHandlerClazz;
    }
}
