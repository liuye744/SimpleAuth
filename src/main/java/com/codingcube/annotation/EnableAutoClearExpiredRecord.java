package com.codingcube.annotation;

import com.codingcube.scheduled.AutoClearExpiredRecordScheduled;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableScheduling
@Import(AutoClearExpiredRecordScheduled.class)
public @interface EnableAutoClearExpiredRecord {
}
