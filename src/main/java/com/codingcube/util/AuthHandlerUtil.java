package com.codingcube.util;

import com.codingcube.exception.PermissionsException;
import com.codingcube.exception.TargetNotFoundException;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import com.codingcube.strategic.SignStrategic;
import org.aspectj.lang.ProceedingJoinPoint;
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
    public static void handlerChain(AutoAuthHandlerChain autoAuthHandlerChain, ApplicationContext applicationContext, HttpServletRequest request, String permissions){
        final List<Object> autoAuthServiceList = autoAuthHandlerChain.getAutoAuthServiceList();
        autoAuthServiceList.forEach(
                (item)->{
                    final AutoAuthHandler autoAuth;
                    if (item instanceof String ){
                        //item is BeanName
                        autoAuth = applicationContext.getBean((String) item, AutoAuthHandler.class);

                    }else if (item instanceof Class){
                        //item is class of AutoAuthService
                        autoAuth = applicationContext.getBean((Class<? extends AutoAuthHandler>) item);
                    }else {
                        throw new TargetNotFoundException("handlerChain error. The value can only be String or Class<? extends AutoAuthHandler>");
                    }

                    if (!autoAuth.isAuthor(request, permissions)){
                        //Permission not met
                        throw new PermissionsException("lack of permissions");
                    }
                }
        );

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
}
