package com.codingcube.aspect;

import com.codingcube.annotation.IsLimit;
import com.codingcube.exception.AccessIsRestricted;
import com.codingcube.properties.LimitInfoUtil;
import com.codingcube.strategic.SignStrategic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class AutoLimit {
    @Resource
    private ApplicationContext applicationContext;

    @Around("@within(isLimit)")
    public Object isLimitClass(ProceedingJoinPoint joinPoint, IsLimit isLimit) throws Throwable {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final Boolean addRecord =  addRecord(className, isLimit, joinPoint);
        if (addRecord){
            return joinPoint.proceed();
        }
        throw new AccessIsRestricted();
    }

    @Around("@annotation(isLimit)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, IsLimit isLimit) throws Throwable{
        final String className =  joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        final Boolean addRecord = addRecord(className+"."+methodName, isLimit, joinPoint);
        if (addRecord){
            return joinPoint.proceed();
        }
        throw new AccessIsRestricted();
    }

    public Boolean addRecord(String recordItem, IsLimit isLimit, ProceedingJoinPoint joinPoint) throws Throwable{
        final int limit = isLimit.value();
        final int seconds = isLimit.seconds();
        final int ban = isLimit.ban();
        final Class<? extends SignStrategic> signStrategic = isLimit.signStrategic();
        final Method signMethod = signStrategic.getMethod("sign",HttpServletRequest.class, ProceedingJoinPoint.class);
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        final SignStrategic signStrategicInstance = signStrategic.getConstructor().newInstance();
        final String sign = (String) signMethod.invoke(signStrategicInstance, request, joinPoint);
        return LimitInfoUtil.addRecord(recordItem, sign, limit, seconds, ban);
    }
}
