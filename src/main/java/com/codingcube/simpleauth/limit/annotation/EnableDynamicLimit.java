package com.codingcube.simpleauth.limit.annotation;

import com.codingcube.simpleauth.limit.dynamic.sign.DynamicLimitSignBean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on goble limit *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({DynamicLimitSignBean.class})
public @interface EnableDynamicLimit {
}
