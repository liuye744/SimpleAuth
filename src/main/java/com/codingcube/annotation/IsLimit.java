package com.codingcube.annotation;

import com.codingcube.strategic.DefaultEffectiveStrategic;
import com.codingcube.strategic.DefaultSignStrategic;
import com.codingcube.strategic.EffectiveStrategic;
import com.codingcube.strategic.SignStrategic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
    boolean judgeAfterReturn() default false;
}
