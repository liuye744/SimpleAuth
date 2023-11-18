package com.codingcube.simpleauth.limit.aspect;

import com.codingcube.simpleauth.limit.annotation.IsLimit;
import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.limit.annotation.SimpleLimit;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.properties.EvaluationEnvironmentContext;
import com.codingcube.simpleauth.properties.FunctionProper;
import com.codingcube.simpleauth.properties.LimitProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.codingcube.simpleauth.util.NullTarget;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author CodingCube<br>
 * The specific aspect class that executes the @IsLimit annotation*
 */
@Aspect
@Component
public class AutoLimit {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private Environment environment;
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

    @Around("@within(simpleLimit)")
    public Object isLimitClass(ProceedingJoinPoint joinPoint, SimpleLimit simpleLimit) throws Throwable {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        return addRecord(className, simpleLimit, joinPoint);
    }

    @Around("@annotation(simpleLimit)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, SimpleLimit simpleLimit) throws Throwable{
        final String className =  joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        return addRecord(className+"."+methodName, simpleLimit, joinPoint);
    }

    public Object addRecord(String recordItem, IsLimit isLimit, ProceedingJoinPoint joinPoint) throws Throwable{
        //Initialization parameter
        final int limit = isLimit.value();
        final int seconds = isLimit.seconds();
        final int ban = isLimit.ban();
        final boolean judgeAfterReturn = isLimit.judgeAfterReturn();
        final String annotationItem = isLimit.item();
        final Class<? extends SignStrategic> signStrategicClazz = isLimit.signStrategic();
        final Class<? extends EffectiveStrategic> effectiveStrategic = isLimit.effectiveStrategic();
        Class<? extends TokenLimit> tokenLimit = isLimit.tokenLimit();
        final Class<? extends RejectedStratagem> rejected = isLimit.rejected();
        if (tokenLimit == CompleteLimit.class){
            tokenLimit = FunctionProper.getTokenLimitClass();
        }
        return addRecord(joinPoint, recordItem, limit, seconds, ban, judgeAfterReturn, annotationItem, signStrategicClazz, effectiveStrategic, tokenLimit, rejected);
    }

    public Object addRecord(String recordItem, SimpleLimit simpleLimit, ProceedingJoinPoint joinPoint) throws Throwable{
        //Initialization parameter
        final Class<? extends SignStrategic> signStrategicClazz = simpleLimit.signStrategic();
        final Class<? extends EffectiveStrategic> effectiveStrategic = simpleLimit.effectiveStrategic();
        final int limit = simpleLimit.value();
        final int seconds = simpleLimit.seconds();
        final int ban = simpleLimit.ban();
        final boolean judgeAfterReturn = simpleLimit.judgeAfterReturn();
        final String annotationItem = simpleLimit.item();
        Class<? extends TokenLimit> tokenLimit = simpleLimit.tokenLimit();
        final Class<? extends RejectedStratagem> rejected = simpleLimit.rejected();
        if (tokenLimit == CompleteLimit.class){
            tokenLimit = FunctionProper.getTokenLimitClass();
        }
        return addRecord(joinPoint, recordItem, limit, seconds, ban, judgeAfterReturn, annotationItem, signStrategicClazz, effectiveStrategic, tokenLimit, rejected);
    }

    public Object addRecord(ProceedingJoinPoint joinPoint,
                            String recordItem,
                            int limit,
                            int seconds,
                            int ban,
                            boolean judgeAfterReturn,
                            String annotationItem,
                            Class<? extends SignStrategic> signStrategicClazz,
                            Class<? extends EffectiveStrategic> effectiveStrategic,
                            Class<? extends TokenLimit> tokenLimitClazz,
                            Class<? extends RejectedStratagem> rejectedStratagem
    )throws Throwable {
        if (!"".equals(annotationItem)){
            //Analytic SpEL
            try {
                EvaluationContext environmentContext = new EvaluationEnvironmentContext(environment);
                final Expression expression = new SpelExpressionParser().parseExpression(annotationItem);
                recordItem = expression.getValue(environmentContext, String.class);
            }catch (Exception e){
                recordItem = annotationItem;
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        //Create sign
        SignStrategic signStrategic = AuthHandlerUtil.getBean(applicationContext, signStrategicClazz);
        final String sign = signStrategic.sign(request, new SimpleJoinPoint(joinPoint));


        //Verify that this request is recorded.
        EffectiveStrategic effectiveStrategicInstance = AuthHandlerUtil.getBean(applicationContext, effectiveStrategic);

        //Whether effectiveStrategic judges after returning.
        if (!judgeAfterReturn){
            final Boolean isEffective = effectiveStrategicInstance.effective(request, new SimpleJoinPoint(joinPoint),null);
            if (!isEffective){
                LogLimitFormat limitFormat = new LogLimitFormat(limit, seconds, ban, recordItem,
                        signStrategicClazz,sign, "annotation limit",false,
                        effectiveStrategic,true, false, rejectedStratagem);
                log.debug(limitFormat.toString());
                return joinPoint.proceed();
            }
        }

        final Boolean addRecord = LimitInfoUtil.addRecord(recordItem, sign, limit, seconds, ban, tokenLimitClazz);
        if (!addRecord){
            LogLimitFormat limitFormat = new LogLimitFormat(limit, seconds, ban, recordItem, signStrategicClazz,sign,
                    "annotation limit", judgeAfterReturn,effectiveStrategic,true, false, rejectedStratagem);
            log.debug(limitFormat.toString());
            if (rejectedStratagem == NullTarget.class){
                rejectedStratagem = LimitProper.getDefaultRejectedClazz();
            }
            final RejectedStratagem rejectedStratagemBean = AuthHandlerUtil.getBean(applicationContext, rejectedStratagem);
            rejectedStratagemBean.doRejected(request, ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse(), limitFormat);
        }
        final Object result = joinPoint.proceed();
        //Judge whether to delete the record.
        if (judgeAfterReturn){
            final Boolean isEffective = effectiveStrategicInstance.effective(request, new SimpleJoinPoint(joinPoint), result);
            LogLimitFormat limitFormat = new LogLimitFormat(limit, seconds, ban, recordItem, signStrategicClazz,sign,
                    "annotation limit", judgeAfterReturn,effectiveStrategic,isEffective, true, rejectedStratagem);
            log.debug(limitFormat.toString());
            if (!isEffective){
                LimitInfoUtil.delRecord(recordItem, sign);
            }
        }
        return result;

    }
}
