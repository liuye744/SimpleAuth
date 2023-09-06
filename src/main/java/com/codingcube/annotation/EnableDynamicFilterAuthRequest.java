package com.codingcube.annotation;

import com.codingcube.config.FilterAuthWebConfig;
import com.codingcube.scheduled.AutoClearExpiredRecordScheduled;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(FilterAuthWebConfig.class)
public @interface EnableDynamicFilterAuthRequest {
}
