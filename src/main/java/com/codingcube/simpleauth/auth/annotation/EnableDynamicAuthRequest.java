package com.codingcube.simpleauth.auth.annotation;

import com.codingcube.simpleauth.auth.dynamic.FilterAuthWebConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on dynamic rights management *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(FilterAuthWebConfig.class)
public @interface EnableDynamicAuthRequest {
}
