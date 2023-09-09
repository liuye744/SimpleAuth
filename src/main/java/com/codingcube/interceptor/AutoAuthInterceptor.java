package com.codingcube.interceptor;

import com.codingcube.exception.PermissionsException;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.logging.Log;
import com.codingcube.logging.LogFactory;
import com.codingcube.logging.logformat.LogAuthFormat;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

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
            final AutoAuthHandler autoAuthHandler;
            if (handlerClass != null){
                autoAuthHandler = applicationContext.getBean(handlerClass);
            }else {
                autoAuthHandler = (AutoAuthHandler) applicationContext.getBean(handlerBeanName);
            }
            this.handler = autoAuthHandler;
        }

        final ArrayList<String> permissions = this.handler.getPermissions();
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
