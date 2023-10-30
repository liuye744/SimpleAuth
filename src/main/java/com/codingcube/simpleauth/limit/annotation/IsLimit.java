package com.codingcube.simpleauth.limit.annotation;

import com.codingcube.simpleauth.limit.strategic.DefaultEffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.DefaultSignStrategic;
import com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CodingCube<br>
 * Access restriction *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IsLimit {
    //Limit the number of times
    int value() default 100;
    //Record operation time(Limited Time)
    int seconds() default 300;
    //ban time
    int ban() default 0;
    //user Sign generation strategy
    Class<? extends SignStrategic> signStrategic() default DefaultSignStrategic.class;
    //Sign of Controller
    String item() default "";
    //Verify that this request is recorded.
    Class<? extends EffectiveStrategic> effectiveStrategic() default DefaultEffectiveStrategic.class;
    //Whether effectiveStrategic judges after returning.
    boolean judgeAfterReturn() default true;
    //Current limiting algorithm
    Class<? extends TokenLimit> tokenLimit() default CompleteLimit.class;
    //rejectedStratagem
    Class<? extends RejectedStratagem> rejected() default DefaultLimitRejectedStratagem.class;
}
