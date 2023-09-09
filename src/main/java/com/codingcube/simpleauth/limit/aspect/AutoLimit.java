package com.codingcube.simpleauth.limit.aspect;

import com.codingcube.simpleauth.limit.annotation.IsLimit;
import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
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
    private final Log log;

    public AutoLimit(LogFactory logFactory) {
        this.log = logFactory.getLimitLog(this.getClass());
    }

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
                LogLimitFormat limitFormat = new LogLimitFormat(limit, seconds, ban, recordItem, signStrategic,sign, "annotation limit",false,effectiveStrategic,true, false);
                log.debug(limitFormat.toString());
                return joinPoint.proceed();
            }
        }

        final Boolean addRecord = LimitInfoUtil.addRecord(recordItem, sign, limit, seconds, ban);
        if (!addRecord){
            LogLimitFormat limitFormat = new LogLimitFormat(limit, seconds, ban, recordItem, signStrategic,sign,
                    "annotation limit", judgeAfterReturn,effectiveStrategic,true, false);
            log.debug(limitFormat.toString());
            throw new AccessIsRestrictedException();
        }
        final Object result = joinPoint.proceed();
        //Judge whether to delete the record.
        if (judgeAfterReturn){
            final Boolean isEffective = (Boolean) effective.invoke(effectiveStrategicInstance, request, joinPoint, result);
            LogLimitFormat limitFormat = new LogLimitFormat(limit, seconds, ban, recordItem, signStrategic,sign,
                    "annotation limit", judgeAfterReturn,effectiveStrategic,isEffective, true);
            log.debug(limitFormat.toString());
            if (!isEffective){
                LimitInfoUtil.delRecord(recordItem, sign);
            }
        }
        return result;

    }
}
