package com.codingcube.simpleauth.validated.annotation;


import com.codingcube.simpleauth.util.NullTarget;
import com.codingcube.simpleauth.validated.strategic.ValidateRejectedStratagem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface SimpleValidate {
    Class<?> value() default Object.class;
    String[] methods() default {};
    Class<? extends ValidateRejectedStratagem> rejected() default NullTarget.class;
}
