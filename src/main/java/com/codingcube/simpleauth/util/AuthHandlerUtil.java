package com.codingcube.simpleauth.util;

import com.codingcube.simpleauth.SimpleAuthApplication;
import com.codingcube.simpleauth.annotation.SimpleCache;
import com.codingcube.simpleauth.exception.PermissionsException;
import com.codingcube.simpleauth.exception.TargetNotFoundException;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.properties.FunctionProper;
import com.codingcube.simpleauth.util.support.BeanDefinition;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author CodingCube<br>*
 * Utility Class for Handling Handlers*
 */

public class AuthHandlerUtil {
    public static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>(16);
    public static final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(16);

    /**
     * * 处理HandlerChain
     * @param autoAuthHandlerChain HandlerChain
     * @param applicationContext SpringFactory
     * @param request 当前请求的request
     * @param permissions 需要的Permission字符串
     * @param log log类
     * @param source 请求来源
     */
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

    /**
     * 处理Handler*
     * @param request 当前请求的request
     * @param permission 需要的Permission字符串
     * @param handlerClass Handler的类
     * @param applicationContext SpringFactory
     * @param log log类
     * @param source 请求来源
     */
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
        return signStrategicInstance.sign(request, new SimpleJoinPoint(joinPoint));

    }

    /**
     * 获取effectiveStrategic*
     * @param effectiveStrategic Class类
     * @param request 当前请求的request
     * @param joinPoint aspect joinPoint
     * @param result 返回结果
     * @param applicationContext springFactory
     * @return 是否记录
     */
    public static Boolean getEffectiveStrategic(Class<? extends EffectiveStrategic> effectiveStrategic, HttpServletRequest request, ProceedingJoinPoint joinPoint, Object result, ApplicationContext applicationContext){
        //Create sign

        final EffectiveStrategic effectiveStrategicInstance = getBean(applicationContext, effectiveStrategic);
        return effectiveStrategicInstance.effective(request, new SimpleJoinPoint(joinPoint), result);

    }

    /**
     * 获取Bean，先委派Spring，查询Bean，若没有则反射创建 *
     */
    public static <T> T getBean(ApplicationContext applicationContext, Class<T> clazz){
        try{
            return applicationContext.getBean(clazz);
        }catch (NoSuchBeanDefinitionException e){
            try {
                if (FunctionProper.isHandlerCacheStatic()){
                    BeanDefinition beanDefinition = beanDefinitionMap.get(clazz.getName());
                    if (beanDefinition == null){
                        beanDefinition = initBeanDefinition(clazz);
                    }
                    switch (beanDefinition.getType()){
                        default:
                        case 0:
                        case 2:
                            //false & proto
                            return clazz.getConstructor().newInstance();
                        case 1:
                            //singleton
                            final Object obj = beanMap.get(clazz.getName());
                            if (obj != null){
                                return clazz.cast(obj);
                            }
                            synchronized (getSingletonMutex()){
                                final T objInstance = clazz.getConstructor().newInstance();
                                beanMap.put(clazz.getName(), objInstance);
                                return objInstance;
                            }
                        case 3:
                            //request
                            final HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                            final String requestBeanKey = requestBeanKey(clazz);
                            final Object attribute = request.getAttribute(requestBeanKey);
                            if (attribute == null){
                                synchronized (request){
                                    final Object syncAttribute = request.getAttribute(requestBeanKey);
                                    if (syncAttribute == null){
                                        final T objInstance = clazz.getConstructor().newInstance();
                                        request.setAttribute(requestBeanKey, objInstance);
                                        return objInstance;
                                    }else {
                                        return (T) syncAttribute;
                                    }
                                }
                            }else {
                                return (T) attribute;
                            }
                        case 4:
                            //session
                            final HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession(true);
                            final String sessionBeanKey = requestBeanKey(clazz);
                            final Object sessionAttribute = session.getAttribute(sessionBeanKey);
                            if (sessionAttribute == null){
                                synchronized (session){
                                    if (session.getAttribute(sessionBeanKey) == null){
                                        final T objInstance = clazz.getConstructor().newInstance();
                                        session.setAttribute(sessionBeanKey, objInstance);
                                        return objInstance;
                                    }
                                }
                            }else {
                                return (T) sessionAttribute;
                            }

                    }


                }
                return clazz.getConstructor().newInstance();

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new NullPointerException("Required a parameterless constructor");
            }
        }
    }

    public static Object getSingletonMutex() {
        return beanMap;
    }

    public static BeanDefinition initBeanDefinition(Class<?> clazz){
        final SimpleCache simpleCache = clazz.getAnnotation(SimpleCache.class);
        final BeanDefinition beanDefinition;
        if (simpleCache != null){
            beanDefinition = new BeanDefinition(simpleCache.type());
        }else {
            //默认缓存类型
            beanDefinition = new BeanDefinition("singleton");
        }
        beanDefinitionMap.put(clazz.getName(), beanDefinition);
        return beanDefinition;
    }

    public static String requestBeanKey(Class<?> clazz){
        return "SIMPLEAUTH$" + clazz.getName();
    }
    public static String beanKey(Class<?> clazz){
        return clazz.getName();
    }

}
