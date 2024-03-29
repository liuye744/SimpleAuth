package com.codingcube.simpleauth.properties;

import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.auth.strategic.DefaultAuthRejectedStratagem;
import com.codingcube.simpleauth.validated.strategic.DefaultValidateRejectedStratagem;
import com.codingcube.simpleauth.validated.strategic.ValidateRejectedStratagem;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simple-auth.validate")
public class ValidateProper {

    private String defaultRejected = "com.codingcube.simpleauth.validated.strategic.DefaultValidateRejectedStratagem";
    private static Class<? extends ValidateRejectedStratagem> defaultRejectedClazz = DefaultValidateRejectedStratagem.class;

    private String defaultValidateObject = "java.lang.Object";
    private static Class<?> defaultValidateObjectClazz = Object.class;

    public String getDefaultRejected() {
        return defaultRejected;
    }

    public void setDefaultRejected(String defaultRejected) {
        this.defaultRejected = defaultRejected;
    }

    public static Class<? extends ValidateRejectedStratagem> getDefaultRejectedClazz() {
        return defaultRejectedClazz;
    }

    public static void setDefaultRejectedClazz(Class<? extends ValidateRejectedStratagem> defaultRejectedClazz) {
        ValidateProper.defaultRejectedClazz = defaultRejectedClazz;
    }

    public String getDefaultValidateObject() {
        return defaultValidateObject;
    }

    public void setDefaultValidateObject(String defaultValidateObject) {
        this.defaultValidateObject = defaultValidateObject;
    }

    public static Class<?> getDefaultValidateObjectClazz() {
        return defaultValidateObjectClazz;
    }

    public static void setDefaultValidateObjectClazz(Class<?> defaultValidateObjectClazz) {
        ValidateProper.defaultValidateObjectClazz = defaultValidateObjectClazz;
    }
}
