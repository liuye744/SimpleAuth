package com.codingcube.aspect;

import com.codingcube.annotation.IsLimit;
import com.codingcube.exception.AccessIsRestrictedException;
import com.codingcube.properties.LimitInfoUtil;
import com.codingcube.strategic.EffectiveStrategic;
import com.codingcube.strategic.SignStrategic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author CodingCube<br>
 * The specific aspect class that executes the @IsLimit annotation*
 */
@Aspect
@Component
public class AutoLimit {
    @Resource
    private ConfigurableApplicationContext configurableApplicationContext;

    @Around("@within(isLimit)")
    public Object isLimitClass(ProceedingJoinPoint joinPoint, IsLimit isLimit) throws Throwable {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        return addRecord(className, isLimit, joinPoint);
    }

    @Around("@annotation(isLimit)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, IsLimit isLimit) throws Throwable{
        final String className =  joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        return addRecord(className+"."+methodName, isLimit, joinPoint);
    }

    public Object addRecord(String recordItem, IsLimit isLimit, ProceedingJoinPoint joinPoint) throws Throwable{
        //Initialization parameter
        final int limit = isLimit.value();
        final int seconds = isLimit.seconds();
        final int ban = isLimit.ban();
        final boolean judgeAfterReturn = isLimit.judgeAfterReturn();
        final String annotationItem = isLimit.item();
        if (!"".equals(annotationItem)){
            //Analytic SpEL
            recordItem = annotationItem;
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        //Create sign
        final Class<? extends SignStrategic> signStrategic = isLimit.signStrategic();
        final Method signMethod = signStrategic.getMethod("sign", HttpServletRequest.class, ProceedingJoinPoint.class);
        final SignStrategic signStrategicInstance = signStrategic.getConstructor().newInstance();
        final String sign = (String) signMethod.invoke(signStrategicInstance, request, joinPoint);

        //Verify that this request is recorded.
        final Class<? extends EffectiveStrategic> effectiveStrategic = isLimit.effectiveStrategic();
        final Method effective = effectiveStrategic.getMethod("effective", HttpServletRequest.class, ProceedingJoinPoint.class, Object.class);
        final EffectiveStrategic effectiveStrategicInstance = effectiveStrategic.getConstructor().newInstance();

        //Whether effectiveStrategic judges after returning.
        if (!judgeAfterReturn){
            final Boolean isEffective = (Boolean) effective.invoke(effectiveStrategicInstance, request, joinPoint, null);
            if (!isEffective){
                return joinPoint.proceed();
            }
        }

        final Boolean addRecord = LimitInfoUtil.addRecord(recordItem, sign, limit, seconds, ban);

        if (!addRecord){
            throw new AccessIsRestrictedException();
        }
        final Object result = joinPoint.proceed();
        //Judge whether to delete the record.
        if (judgeAfterReturn){
            final Boolean isEffective = (Boolean) effective.invoke(effectiveStrategicInstance, request, joinPoint, result);

            if (!isEffective){
                LimitInfoUtil.delRecord(recordItem, sign);
            }
        }
        return result;

    }
}
