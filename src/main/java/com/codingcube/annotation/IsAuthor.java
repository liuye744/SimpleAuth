package com.codingcube.annotation;

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
    Class<? extends AutoAuthService>[] authentication() default DefaultAuthService.class;
}