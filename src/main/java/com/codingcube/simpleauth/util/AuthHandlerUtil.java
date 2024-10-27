package com.codingcube.simpleauth.util;

import com.codingcube.simpleauth.annotation.SimpleCache;
import com.codingcube.simpleauth.auth.dynamic.RequestAuthItem;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.autoconfig.domain.Handler;
import com.codingcube.simpleauth.autoconfig.domain.SimpleAuthConfig;
import com.codingcube.simpleauth.autoconfig.execption.ConfigurationParseException;
import com.codingcube.simpleauth.autoconfig.factory.ConfigFactory;
import com.codingcube.simpleauth.autoconfig.json.JSON2SimpleAuthObject;
import com.codingcube.simpleauth.autoconfig.xml.XML2SimpleAuthObject;
import com.codingcube.simpleauth.exception.TargetNotFoundException;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;
import com.codingcube.simpleauth.properties.AuthProper;
import com.codingcube.simpleauth.properties.FunctionProper;
import com.codingcube.simpleauth.util.support.scope.*;
import com.codingcube.simpleauth.util.support.BeanDefinition;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CodingCube<br>*
 * Utility Class for Handling Handlers*
 */
public class AuthHandlerUtil {
    public static final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(16);
    public static final HashMap<String, Scope> scopes = new HashMap<>();
    public static SimpleAuthConfig simpleAuthConfig;

    static {
        //init scopes
        scopes.put("SINGLETON", new SingletonScope());
        scopes.put("FALSE", new FalseScope());
        scopes.put("PROTO", new ProtoScope());
        scopes.put("REQUEST", new RequestScope());
        scopes.put("SESSION", new SessionScope());


        InputStream simpleauthFile = AuthHandlerUtil.class.getClassLoader().getResourceAsStream("simpleauth.xml");
        if (simpleauthFile != null){
            ConfigFactory factory = new ConfigFactory(XML2SimpleAuthObject.class);
            simpleAuthConfig = factory.getConfig("simpleauth.xml");
        }else {
            simpleauthFile = AuthHandlerUtil.class.getClassLoader().getResourceAsStream("simpleauth.json");
            if (simpleauthFile != null){
                ConfigFactory factory = new ConfigFactory(JSON2SimpleAuthObject.class);
                simpleAuthConfig = factory.getConfig("simpleauth.json");
            }
        }
        if (simpleAuthConfig != null){
            //初始化SimpleAuthBean
            //初始化handler Limit Clazz2Id
            //handler
            {
                final Map<String, Handler> handlerMap = simpleAuthConfig.getHandlerMap();
                handlerMap.forEach((key,value) -> cacheSimpleAuthBean(initBeanDefinition(value)));
            }
        }
    }
    /**
     * * 处理HandlerChain
     * @param autoAuthHandlerChain HandlerChain
     * @param applicationContext SpringFactory
     * @param request 当前请求的request
     * @param permissions 需要的Permission字符串
     * @param log log类
     * @param source 请求来源
     */
    public static void handlerChain(AutoAuthHandlerChain autoAuthHandlerChain,
                                    ApplicationContext applicationContext,
                                    HttpServletRequest request,
                                    String permissions,
                                    Class<? extends AuthRejectedStratagem> rejectClass,
                                    Log log,
                                    String source){
        final List<Object> autoAuthServiceList = autoAuthHandlerChain.getAutoAuthServiceList();
        for (Object item : autoAuthServiceList) {
            AutoAuthHandler autoAuth;
            if (item instanceof String) {
                //item is BeanName
                autoAuth = applicationContext.getBean((String) item, AutoAuthHandler.class);
            } else if (item instanceof Class) {
                //item is class of AutoAuthService
                autoAuth = getBean(applicationContext, (Class<? extends AutoAuthHandler>) item);
            } else {
                throw new TargetNotFoundException("handlerChain error. The value can only be String or Class<? extends AutoAuthHandler>");
            }
            boolean author = false;
            try{
                author = autoAuth.isAuthor(request, permissions);
            }catch (Exception e){
                LogAuthFormat logAuthFormat = new LogAuthFormat(request, source + " handlerChain " + autoAuthHandlerChain.getClass().getName(), author, autoAuth.getClass().getName(), permissions, rejectClass);
                log.debug(logAuthFormat.toString());
                throw e;
            }
            LogAuthFormat logAuthFormat = new LogAuthFormat(request, source + " handlerChain " + autoAuthHandlerChain.getClass().getName(), author, autoAuth.getClass().getName(), permissions, rejectClass);
            log.debug(logAuthFormat.toString());
            if (!author) {
                //Permission not met
                if (rejectClass == NullTarget.class){
                    rejectClass = AuthProper.getDefaultRejectedClazz();
                }
                final AuthRejectedStratagem rejectedStratagem = AuthHandlerUtil.getBean(applicationContext, rejectClass);
                rejectedStratagem.doRejected(request,
                        ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse(),
                        logAuthFormat);
            }
        }

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
    public static void handler(HttpServletRequest request,
                               String permission,
                               Class<? extends AutoAuthHandler> handlerClass,
                               ApplicationContext applicationContext,
                               Class<? extends AuthRejectedStratagem> rejectClass,
                               Log log, String source) {
        AutoAuthHandler authHandler = getBean(applicationContext, handlerClass);
        boolean author = false;
        try{
            author = authHandler.isAuthor(request, permission);
        }catch(Exception e){
            //Handler中抛出异常前打印日志
            LogAuthFormat logAuthFormat = new LogAuthFormat(request, source+" handler", author,handlerClass.getName(), permission, rejectClass);
            log.debug(logAuthFormat.toString());
            throw e;
        }
        LogAuthFormat logAuthFormat = new LogAuthFormat(request, source+" handler", author,handlerClass.getName(), permission, rejectClass);
        log.debug(logAuthFormat.toString());
        if (!author){
            if (rejectClass == NullTarget.class){
                rejectClass = AuthProper.getDefaultRejectedClazz();
            }
            final AuthRejectedStratagem rejectedStratagem = AuthHandlerUtil.getBean(applicationContext, rejectClass);
            rejectedStratagem.doRejected(request,
                    ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse(),
                    logAuthFormat);
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
            if (FunctionProper.isHandlerCacheStatic()){
                BeanDefinition beanDefinition = beanDefinitionMap.get(clazz.getName());
                if (beanDefinition == null){
                    beanDefinition = initBeanDefinition(clazz);
                }
                final Scope scope = scopes.get(beanDefinition.getType().name());
                if (scope == null){
                    throw new NullPointerException(beanDefinition.getType() + " cant find");
                }
                return scope.getBean(beanDefinition);
            }
            return getParameterlessObject(clazz);
        }
    }

    public static void doRequestAuthItemList(List<RequestAuthItem> requestAuthItem,
                                             AntPathMatcher antPathMatcher,
                                             HttpServletRequest request,
                                             ApplicationContext applicationContext,
                                             Log log,
                                             String source){
        for (RequestAuthItem authItem : requestAuthItem) {
            final List<String> paths = authItem.getPath();
            for (String path: paths){
                if (antPathMatcher.match(path, request.getRequestURI())){
                    final String permission = authItem.getPermission();
                    final Class<? extends AutoAuthHandler> handlerClass = authItem.getHandlerClass();
                    final Class<? extends AutoAuthHandlerChain> handlerChainClass = authItem.getHandlerChainClass();
                    final AutoAuthHandler handler = authItem.getHandler();
                    final AutoAuthHandlerChain handlerChain = authItem.getHandlerChain();
                    final Class<? extends AuthRejectedStratagem> rejected = authItem.getRejected();
                    if (handlerChain != null){
                        AuthHandlerUtil.handlerChain(handlerChain, applicationContext, request, permission,rejected, log, source);
                    }else if (handlerClass != null){
                        //deal with Handler
                        AuthHandlerUtil.handler(request, permission, handlerClass, applicationContext,rejected, log, source);
                    }else if (handlerChainClass != null){
                        //deal with handlerChain
                        final AutoAuthHandlerChain authHandlerChain = AuthHandlerUtil.getBean(applicationContext, handlerChainClass);
                        AuthHandlerUtil.handlerChain(authHandlerChain, applicationContext, request, permission,rejected, log, source);
                    }else {
                        //parameter error
                        throw new InvalidParameterException("Requires either AutoAuthHandler or AutoAuthHandlerChain");
                    }
                }

            }
        }

    }


    /**
     * init bean definition by class
     * @param clazz class
     * @return BeanDefinition
     */
    private static BeanDefinition initBeanDefinition(Class<?> clazz){
        final SimpleCache simpleCache = clazz.getAnnotation(SimpleCache.class);
        final BeanDefinition beanDefinition;
        if (simpleCache != null){
            final ScopeType scopeType = ScopeType.valueOf(simpleCache.type());
            beanDefinition = new BeanDefinition(clazz, scopeType);
        }else {
            //默认缓存类型
            beanDefinition = new BeanDefinition(clazz);
        }
        beanDefinitionMap.put(clazz.getName(), beanDefinition);
        return beanDefinition;
    }

    /**
     * init bean definition by simpleAuth handler
     * @param handler simpleAuth handler
     * @return BeanDefinition
     */
    private static BeanDefinition initBeanDefinition(Handler handler){
        final Class<? extends AutoAuthHandler> handlerClass = handler.getHandlerClass();
        final ScopeType scopeType = ScopeType.valueOf(handler.getScope().toUpperCase());
        return new BeanDefinition(handlerClass, handler.getId(), scopeType);
    }

    /**
     * init simpleAuth bean
     * @param beanDefinition beanDefinition
     */
    public static void cacheSimpleAuthBean(BeanDefinition beanDefinition){
        //初始化BeanDefinition
        beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
        //初始化singleton模式
        final Scope scope = scopes.get(beanDefinition.getType().name());
        scope.initBean(beanDefinition);
    }


    /**
     * request和session作用域的key生成策略
     * @param clazz clazz
     * @return key
     */
    public static String requestBeanKey(Class<?> clazz){
        return "SIMPLEAUTH$" + clazz.getName();
    }
    public static String beanKey(Class<?> clazz){
        return clazz.getName();
    }

    /**
     * 获取无参构造方法
     */
    public static <T> T getParameterlessObject(Class<? extends T> clazz){
        try {
            return clazz.getConstructor().newInstance();
        }catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new NullPointerException("Required a parameterless constructor");
        }
    }

    public static String getClassName(Class<?> clazz){
        return clazz==null? "null":clazz.getName();
    }
}
