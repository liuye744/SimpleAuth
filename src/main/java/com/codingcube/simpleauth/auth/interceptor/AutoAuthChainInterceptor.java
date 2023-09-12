package com.codingcube.simpleauth.auth.interceptor;


import com.codingcube.simpleauth.auth.PermissionOperate;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * @author CodingCube<br>
 * The HandlerChain Interceptor of SimpleAuth<br>
 * Implementing Rapid Interceptor Configuration Functionality*

 */
public class AutoAuthChainInterceptor  implements HandlerInterceptor {
    private final Class<? extends AutoAuthHandlerChain> handlerChainClass;
    private final ApplicationContext applicationContext;
    private final Log log;

    public AutoAuthChainInterceptor(Class<? extends AutoAuthHandlerChain> handlerChainClass, ApplicationContext applicationContext, LogFactory logFactory) {
        this.handlerChainClass = handlerChainClass;
        this.applicationContext = applicationContext;
        this.log = logFactory.getLog(this.getClass());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AutoAuthHandlerChain autoAuthHandlerChain = AuthHandlerUtil.getBean(applicationContext, handlerChainClass);
        final String permissions = (String) request.getAttribute(PermissionOperate.PERMISSIONS);
        AuthHandlerUtil.handlerChain(autoAuthHandlerChain, applicationContext, request, permissions, log, "SimpleAuth Interceptor");
        return true;
    }
}
