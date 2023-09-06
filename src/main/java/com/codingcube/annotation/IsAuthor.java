package com.codingcube.annotation;

import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import com.codingcube.handler.DefaultAuthHandler;
import com.codingcube.handler.DefaultAuthHandlerChain;
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
public @interface IsAuthor {
    //permission
    String value() default "";
    //auth Handler Class
    Class<? extends AutoAuthHandler>[] authentication() default DefaultAuthHandler.class;
    //Multiple auth Handler Class
    Class<? extends AutoAuthHandlerChain>[] authentications() default DefaultAuthHandlerChain.class;
}