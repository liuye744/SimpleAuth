package com.codingcube.simpleauth.util;

import com.codingcube.simpleauth.exception.PermissionsException;
import com.codingcube.simpleauth.exception.TargetNotFoundException;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author CodingCube<br>*
 * Utility Class for Handling Handlers*
 */
public class AuthHandlerUtil {
    public static void handlerChain(AutoAuthHandlerChain autoAuthHandlerChain, ApplicationContext applicationContext, HttpServletRequest request, String permissions, Log log, String source){
        final List<Object> autoAuthServiceList = autoAuthHandlerChain.getAutoAuthServiceList();
        autoAuthServiceList.forEach(
                (item)->{
                    AutoAuthHandler autoAuth;
                    if (item instanceof String ){
                        //item is BeanName
                        autoAuth = applicationContext.getBean((String) item, AutoAuthHandler.class);
                    }else if (item instanceof Class){
                        //item is class of AutoAuthService
                        autoAuth = getBean(applicationContext, (Class) item);
                    }else {
                        throw new TargetNotFoundException("handlerChain error. The value can only be String or Class<? extends AutoAuthHandler>");
                    }
                    final boolean author = autoAuth.isAuthor(request, permissions);
                    LogAuthFormat logAuthFormat = new LogAuthFormat(request, source+ " handlerChain "+autoAuthHandlerChain.getClass().getName(), author, autoAuth.getClass().getName(), permissions);
                    log.debug(logAuthFormat.toString());
                    if (!author){
                        //Permission not met
                        throw new PermissionsException("lack of permissions");
                    }
                }
        );

    }

    public static void handler(HttpServletRequest request, String permission, Class<? extends AutoAuthHandler> handlerClass, ApplicationContext applicationContext, Log log, String source) {
        AutoAuthHandler authHandler = getBean(applicationContext, handlerClass);
        final boolean author = authHandler.isAuthor(request, permission);
        LogAuthFormat logAuthFormat = new LogAuthFormat(request, source+" handler", author,handlerClass.getName(), permission);
        log.debug(logAuthFormat.toString());
        if (!author){
            throw new PermissionsException("lack of permissions");
        }
    }

    public static String getSignStrategic(Class<? extends SignStrategic> signStrategic, HttpServletRequest request, ProceedingJoinPoint joinPoint){
        //Create sign
        try{
            final Method signMethod = signStrategic.getMethod("sign", HttpServletRequest.class, ProceedingJoinPoint.class);
            final SignStrategic signStrategicInstance = signStrategic.getConstructor().newInstance();
            return  (String) signMethod.invoke(signStrategicInstance, request, joinPoint);

        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return "";
        }
    }

    public static Boolean getEffectiveStrategic(Class<? extends EffectiveStrategic> effectiveStrategic, HttpServletRequest request, ProceedingJoinPoint joinPoint, Object result){
        //Create sign
        try{
            final Method signMethod = effectiveStrategic.getMethod("effective", HttpServletRequest.class, ProceedingJoinPoint.class, Object.class);
            final EffectiveStrategic effectiveStrategicInstance = effectiveStrategic.getConstructor().newInstance();
            return (Boolean) signMethod.invoke(effectiveStrategicInstance, request, joinPoint, result);

        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return true;
        }
    }
    public static <T> T getBean(ApplicationContext applicationContext, Class clazz){
        try{
            return (T) applicationContext.getBean(clazz);
        }catch (NoSuchBeanDefinitionException e){
            try {
                return (T) clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new NullPointerException();
            }
        }
    }
}
