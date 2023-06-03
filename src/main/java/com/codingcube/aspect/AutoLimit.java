package com.codingcube.aspect;

import com.codingcube.annotation.IsAuthor;
import com.codingcube.annotation.IsLimit;
import com.codingcube.exception.AccessIsRestricted;
import com.codingcube.exception.PermissionsException;
import com.codingcube.properties.LimitInfoMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
public class AutoLimit {
    @Resource
    private ApplicationContext applicationContext;

    @Around("@within(isLimit)")
    public Object isLimitClass(ProceedingJoinPoint joinPoint, IsLimit isLimit) throws Throwable {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        final String addr = request.getRemoteAddr();

        final int limit = isLimit.value();
        final int seconds = isLimit.seconds();
        final Boolean addRecord = LimitInfoMap.addRecord(className, addr, limit, seconds);
        if (addRecord){
            return joinPoint.proceed();
        }
        throw new AccessIsRestricted();
    }

    @Around("@annotation(isLimit)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, IsLimit isLimit) throws Throwable{
        final String className =  joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        final String addr = request.getRemoteAddr();

        final int limit = isLimit.value();
        final int seconds = isLimit.seconds();
        final Boolean addRecord = LimitInfoMap.addRecord(className+"."+methodName, addr, limit, seconds);
        if (addRecord){
            return joinPoint.proceed();
        }
        throw new AccessIsRestricted();
    }
}
