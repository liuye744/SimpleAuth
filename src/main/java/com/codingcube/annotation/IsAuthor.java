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
 * @author dhc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IsAuthor {
    //所需权限
    String value() default "";
    //鉴权方法
    Class<? extends AutoAuthHandler>[] authentication() default DefaultAuthHandler.class;
    //鉴权链
    Class<? extends AutoAuthHandlerChain>[] authentications() default DefaultAuthHandlerChain.class;
}