package com.codingcube.simpleauth.limit.annotation;

import com.codingcube.simpleauth.limit.dynamic.DynamicLimitConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on goble limit *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({DynamicLimitConfig.class})
public @interface EnableDynamicLimit {
}
