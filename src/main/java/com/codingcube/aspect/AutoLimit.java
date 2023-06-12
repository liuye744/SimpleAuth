package com.codingcube.aspect;

import com.codingcube.annotation.IsLimit;
import com.codingcube.exception.AccessIsRestricted;
import com.codingcube.properties.LimitInfoUtil;
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

        final Boolean addRecord =  addRecord(className, addr, isLimit);
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
        final Boolean addRecord = addRecord(className+"."+methodName, addr, isLimit);
        if (addRecord){
            return joinPoint.proceed();
        }
        throw new AccessIsRestricted();
    }

    public Boolean addRecord(String recordItem, String sign, IsLimit isLimit) throws Throwable{
        final int limit = isLimit.value();
        final int seconds = isLimit.seconds();
        final int ban = isLimit.ban();
        return LimitInfoUtil.addRecord(recordItem, sign, limit, seconds, ban);
    }
}
