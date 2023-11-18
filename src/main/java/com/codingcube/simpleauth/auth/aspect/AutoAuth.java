package com.codingcube.simpleauth.auth.aspect;

import com.codingcube.simpleauth.auth.annotation.SimpleAuth;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.annotation.IsAuthor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.auth.handler.DefaultAuthHandler;
import com.codingcube.simpleauth.auth.handler.DefaultAuthHandlerChain;
import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.autoconfig.domain.Handler;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.properties.AuthProper;
import com.codingcube.simpleauth.properties.EvaluationEnvironmentContext;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author CodingCube<br>
 * The specific aspect class that executes the @IsAuthor annotation*
 */
@Aspect
@Component
public class AutoAuth {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private Environment environment;
    Log log;

    public AutoAuth(LogFactory logFactory) {
        this.log = logFactory.getLog(this.getClass());
    }

    @Around("@within(isAuthor)")
    public Object isAuthorClass(ProceedingJoinPoint joinPoint, IsAuthor isAuthor) throws Throwable {
        final Class<? extends AutoAuthHandler>[] autoAuthServices = isAuthor.handler();
        final Class<? extends AutoAuthHandlerChain>[] authentications = isAuthor.handlerChain();
        String permissions = isAuthor.value();
        try {
            EvaluationContext environmentContext = new EvaluationEnvironmentContext(environment);
            final Expression expression = new SpelExpressionParser().parseExpression(permissions);
            permissions = expression.getValue(environmentContext, String.class);
        }catch (Exception ignored){
        }
        final Class<? extends AuthRejectedStratagem> rejected = isAuthor.rejected();

        return doAuth(joinPoint, autoAuthServices, authentications, permissions, rejected);
    }
    @Around("@annotation(isAuthor)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, IsAuthor isAuthor) throws Throwable{
        return isAuthorClass(joinPoint, isAuthor);
    }

    @Around("@within(simpleAuth)")
    public Object isAuthorClass(ProceedingJoinPoint joinPoint, SimpleAuth simpleAuth) throws Throwable {
        final Class<? extends AutoAuthHandler>[] autoAuthServices = simpleAuth.handler();
        final Class<? extends AutoAuthHandlerChain>[] authentications = simpleAuth.handlerChain();
        String permissions = simpleAuth.value();
        try {
            EvaluationContext environmentContext = new EvaluationEnvironmentContext(environment);
            final Expression expression = new SpelExpressionParser().parseExpression(permissions);
            permissions = expression.getValue(environmentContext, String.class);
        }catch (Exception ignored){
        }
        final Class<? extends AuthRejectedStratagem> rejected = simpleAuth.rejected();

        return doAuth(joinPoint, autoAuthServices, authentications, permissions, rejected);
    }
    @Around("@annotation(simpleAuth)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, SimpleAuth simpleAuth) throws Throwable{
        return isAuthorClass(joinPoint, simpleAuth);
    }


    public Object doAuth(ProceedingJoinPoint joinPoint,
                         Class<? extends AutoAuthHandler>[] handlerClass,
                         Class<? extends AutoAuthHandlerChain>[] handlerChainClass,
                         String permissions,
                         Class<? extends AuthRejectedStratagem> reject
    )throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //authenticate authority
        //If the handlerChain is configured and handler is not configured, the DefaultAutoAuthService will be abandoned.
        boolean isExecuteDefault = true;
        final Class<?> defaultAnnotationHandlerClass = DefaultAuthHandler.class;
        final Class<?> defaultAnnotationHandlerChainClass = DefaultAuthHandlerChain.class;

        if (
            //handlerChain parameter is not the default.
            !defaultAnnotationHandlerChainClass.equals(handlerChainClass[0])
                    &&
            //handler parameter is the default
            handlerClass.length == 1 &&
            defaultAnnotationHandlerClass.equals(handlerClass[0])
        )
        {
            isExecuteDefault = false;
        }
        //execute handler
        if (isExecuteDefault){
            if (handlerClass.length == 1 && defaultAnnotationHandlerClass == handlerClass[0]){
                handlerClass[0] = AuthProper.getDefaultHandlerClazz();
            }
            Arrays.stream(handlerClass).forEach(
                    (item)-> AuthHandlerUtil.handler(request, permissions, item, applicationContext, reject, log, "annotation")
            );
        }
        //execute handlerChain
        Arrays.stream(handlerChainClass).forEach(
                (items) -> {
                    AutoAuthHandlerChain autoAuthHandlerChain = AuthHandlerUtil.getBean(applicationContext, items);
                    AuthHandlerUtil.handlerChain(autoAuthHandlerChain, applicationContext, request, permissions, reject, log, "annotation");
                }
        );
        return joinPoint.proceed();
    }
}