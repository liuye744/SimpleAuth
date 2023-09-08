package com.codingcube.aspect;

import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.annotation.IsAuthor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.codingcube.handler.AutoAuthHandlerChain;
import com.codingcube.logging.Log;
import com.codingcube.logging.LogFactory;
import com.codingcube.util.AuthHandlerUtil;
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
        final Class<? extends AutoAuthHandler>[] autoAuthServices = isAuthor.authentication();
        final Class<? extends AutoAuthHandlerChain>[] authentications = isAuthor.authentications();

        final String permissions = isAuthor.value();

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //authenticate authority
        //If the AutoAuthChain is configured and autoAuthService is not configured, the DefaultAutoAuthService will be abandoned.
        boolean isExecuteDefault = true;
        try {
            if (
                    //authentications(AutoAuthChain) parameter is the default.
                    !((Class[])IsAuthor.class.getMethod("authentications").getDefaultValue())[0].getName()
                    .equals(authentications[0].getName())
                    &&
                    //authentications(AutoAuthService) parameter is not the default
                    ((Class[])IsAuthor.class.getMethod("authentication").getDefaultValue())[0].getName()
                            .equals(autoAuthServices[0].getName())
            )

            {
                    isExecuteDefault = false;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //execute autoAuthService
        if (isExecuteDefault){
            Arrays.stream(autoAuthServices).forEach(
                    (item)->{
                        AuthHandlerUtil.handler(request, permissions, item, applicationContext, log, "annotation");
                    }
            );
        }
        //execute autoAuthChain
        Arrays.stream(authentications).forEach(
                (items)->{
                    final AutoAuthHandlerChain autoAuthHandlerChain = applicationContext.getBean(items);
                    AuthHandlerUtil.handlerChain(autoAuthHandlerChain, applicationContext, request, permissions, log, "annotation");

                }
        );
        return joinPoint.proceed();
    }
    @Around("@annotation(isAuthor)")
    public Object isAuthorMethod(ProceedingJoinPoint joinPoint, IsAuthor isAuthor) throws Throwable{
        return isAuthorClass(joinPoint, isAuthor);
    }

}