package com.codingcube.annotation;

import com.codingcube.config.FilterAuthWebConfig;
import com.codingcube.config.simpleAuthConfig.GobleLimitConfig;
import com.codingcube.handler.simpleAuthHandler.GobleLimitHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on goble limit *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({GobleLimitConfig.class, GobleLimitHandler.class})
public @interface EnableGobleLimit {
}
