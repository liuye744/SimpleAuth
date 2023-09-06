package com.codingcube.annotation;

import com.codingcube.scheduled.AutoClearExpiredRecordScheduled;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * @author CodingCube<br>
 * Turn on the function of regularly cleaning expired access records.<br>
 * By default, cleanup occurs every Monday at 3:30 AM. You can configure a specific schedule using the 'simple-auth.cron' property in the configuration file.*
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@EnableScheduling
@Import(AutoClearExpiredRecordScheduled.class)
public @interface EnableAutoClearExpiredRecord {
}
