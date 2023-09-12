package com.codingcube.simpleauth.auth.interceptor;

import com.codingcube.simpleauth.exception.PermissionsException;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.exception.TargetNotFoundException;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.logging.logformat.LogAuthFormat;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.fasterxml.jackson.databind.exc.InvalidNullException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author CodingCube<br>
 * The Handler Interceptor of SimpleAuth<br>
 * Implementing Rapid Interceptor Configuration Functionality*
 */
public class AutoAuthInterceptor implements HandlerInterceptor {
    private Class<? extends AutoAuthHandler> handlerClass;
    private String handlerBeanName;
    private AutoAuthHandler handler;
    private final ApplicationContext applicationContext;
    private Log log;

    public AutoAuthInterceptor(Class<? extends AutoAuthHandler> handlerClass, ApplicationContext applicationContext, LogFactory logFactory) {
        this.handlerClass = handlerClass;
        this.applicationContext = applicationContext;
        this.log = logFactory.getLog(this.getClass());
    }

    public AutoAuthInterceptor(String handlerBeanName, ApplicationContext applicationContext) {
        this.handlerBeanName = handlerBeanName;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (this.handler == null){
            AutoAuthHandler autoAuthHandler;
            if (handlerClass != null){
                autoAuthHandler = AuthHandlerUtil.getBean(applicationContext, handlerClass);
            }else if (handlerBeanName != null){
                autoAuthHandler = (AutoAuthHandler) applicationContext.getBean(handlerBeanName);
            }else {
                throw new TargetNotFoundException("need handlerClass or handlerBeanName");
            }
            this.handler = autoAuthHandler;
        }

        final List<String> permissions = this.handler.getPermissions();
        String permissionString = permissions==null?"":permissions.toString();

        final boolean author = this.handler.isAuthor(request, permissionString);
        LogAuthFormat logAuthFormat = new LogAuthFormat(request, "SimpleAuth Interceptor", author, this.handlerClass.getName(), permissionString);
        log.debug(logAuthFormat.toString());
        if (!author){
            throw new PermissionsException("lack of permissions");
        }
        return true;
    }
}
