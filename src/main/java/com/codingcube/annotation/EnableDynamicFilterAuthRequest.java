package com.codingcube.annotation;

import com.codingcube.config.FilterAuthWebConfig;
import com.codingcube.scheduled.AutoClearExpiredRecordScheduled;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on dynamic rights management *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(FilterAuthWebConfig.class)
public @interface EnableDynamicFilterAuthRequest {
}
