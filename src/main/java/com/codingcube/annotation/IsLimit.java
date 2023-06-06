package com.codingcube.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IsLimit {
    //Limit the number of times
    int value() default 100;
    //Record operation time(Limited Time)
    int seconds() default 300;
    //ban time
    int ban() default 0;
}
