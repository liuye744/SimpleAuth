package com.codingcube.simpleauth.auth.annotation;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.auth.handler.DefaultAuthHandler;
import com.codingcube.simpleauth.auth.handler.DefaultAuthHandlerChain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CodingCube<br>
 * Authentication*
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SimpleAuth {
    //permission
    String value() default "";
    //auth Handler Class
    Class<? extends AutoAuthHandler>[] handler() default DefaultAuthHandler.class;
    //Multiple auth Handler Class
    Class<? extends AutoAuthHandlerChain>[] handlerChain() default DefaultAuthHandlerChain.class;
}