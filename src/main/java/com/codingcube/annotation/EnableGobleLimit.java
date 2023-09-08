package com.codingcube.annotation;

import com.codingcube.config.simpleAuthConfig.GobleLimitConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on goble limit *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({GobleLimitConfig.class})
public @interface EnableGobleLimit {
}
