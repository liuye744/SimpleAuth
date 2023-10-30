package com.codingcube.simpleauth.auth.aspect;

import com.codingcube.simpleauth.auth.annotation.SimpleAuth;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.annotation.IsAuthor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.autoconfig.domain.Handler;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.properties.AuthProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
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
    Log log;

    public AutoAuth(LogFactory logFactory) {
        this.log = logFactory.getLog(this.getClass());
    }

    @Around("@within(isAuthor)")
    public Object isAuthorClass(ProceedingJoinPoint joinPoint, IsAuthor isAuthor) throws Throwable {
        final Class<? extends AutoAuthHandler>[] autoAuthServices = isAuthor.handler();
        final Class<? extends AutoAuthHandlerChain>[] authentications = isAuthor.handlerChain();
        final String permissions = isAuthor.value();
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
        final String permissions = simpleAuth.value();
        final Class<? extends AuthRejectedStratagem> rejected = simpleAuth.rejected();

        return doAuth(joinPoint, autoAuthServices, authentications, permissions, rejected);
    }
    @Around("@annotation(simpleAuth)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, SimpleAuth simpleAuth) throws Throwable{
        return isAuthorClass(joinPoint, simpleAuth);
    }


    public Object doAuth(ProceedingJoinPoint joinPoint,
                         Class<? extends AutoAuthHandler>[] autoAuthServices,
                         Class<? extends AutoAuthHandlerChain>[] authentications,
                         String permissions,
                         Class<? extends AuthRejectedStratagem> reject
    )throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //authenticate authority
        //If the AutoAuthChain is configured and autoAuthService is not configured, the DefaultAutoAuthService will be abandoned.
        boolean isExecuteDefault = true;
        Class<?> defaultAnnotationHandlerClass = ((Class<?>[])IsAuthor.class.getMethod("handler").getDefaultValue())[0];
        final Class<?> defaultAnnotationHandlerChainClass = ((Class<?>[]) IsAuthor.class.getMethod("handlerChain").getDefaultValue())[0];

        if (
            //authentications(AutoAuthChain) parameter is the default.
                !defaultAnnotationHandlerChainClass
                        .equals(authentications[0])
                        &&
                        //authentications(AutoAuthService) parameter is not the default
                        defaultAnnotationHandlerClass
                                .equals(autoAuthServices[0])
        )

        {
            isExecuteDefault = false;
        }
        //execute autoAuthService
        if (isExecuteDefault){
            if (autoAuthServices.length != 0 && defaultAnnotationHandlerClass == autoAuthServices[0]){
                autoAuthServices[0] = AuthProper.getDefaultHandlerClazz();
            }
            Arrays.stream(autoAuthServices).forEach(
                    (item)-> AuthHandlerUtil.handler(request, permissions, item, applicationContext, reject, log, "annotation")
            );
        }
        //execute autoAuthChain
        Arrays.stream(authentications).forEach(
                (items) -> {
                    AutoAuthHandlerChain autoAuthHandlerChain = AuthHandlerUtil.getBean(applicationContext, items);
                    AuthHandlerUtil.handlerChain(autoAuthHandlerChain, applicationContext, request, permissions, reject, log, "annotation");
                }
        );
        return joinPoint.proceed();
    }
}