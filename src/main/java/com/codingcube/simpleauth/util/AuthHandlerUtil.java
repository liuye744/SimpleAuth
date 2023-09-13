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
import org.springframework.core.ResolvableType;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author CodingCube<br>*
 * Utility Class for Handling Handlers*
 */

public class AuthHandlerUtil {
    public static ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>(16);
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
                        autoAuth = getBean(applicationContext, (Class<? extends AutoAuthHandler>) item);
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

    public static String getSignStrategic(Class<? extends SignStrategic> signStrategic, HttpServletRequest request, ProceedingJoinPoint joinPoint, ApplicationContext application){
        //Create sign
        final SignStrategic signStrategicInstance = getBean(application, signStrategic);
        return signStrategicInstance.sign(request, joinPoint);

    }

    public static Boolean getEffectiveStrategic(Class<? extends EffectiveStrategic> effectiveStrategic, HttpServletRequest request, ProceedingJoinPoint joinPoint, Object result, ApplicationContext applicationContext){
        //Create sign

        final EffectiveStrategic effectiveStrategicInstance = getBean(applicationContext, effectiveStrategic);
        return effectiveStrategicInstance.effective(request, joinPoint, result);

    }

    public static <T> T getBean(ApplicationContext applicationContext, Class<T> clazz){
        try{
            return applicationContext.getBean(clazz);
        }catch (NoSuchBeanDefinitionException e){
            try {
                final Object obj = beanMap.get(clazz.getName());
                if (obj != null){
                    return clazz.cast(obj);
                }
                final T objInstance = clazz.getConstructor().newInstance();
                beanMap.put(clazz.getName(), objInstance);
                return objInstance;

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new NullPointerException("Required a parameterless constructor");
            }
        }
    }

}
