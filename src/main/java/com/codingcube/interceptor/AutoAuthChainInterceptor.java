package com.codingcube.interceptor;

import com.codingcube.exception.PermissionsException;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import com.codingcube.handler.DefaultAuthHandler;
import com.codingcube.implement.PermissionOperate;
import com.codingcube.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CodingCube<br>
 * The HandlerChain Interceptor of SimpleAuth<br>
 * Implementing Rapid Interceptor Configuration Functionality*

 */
public class AutoAuthChainInterceptor  implements HandlerInterceptor {
    private final Class<? extends AutoAuthHandlerChain> handlerChainClass;
    private final ApplicationContext applicationContext;

    public AutoAuthChainInterceptor(Class<? extends AutoAuthHandlerChain> handlerChainClass, ApplicationContext applicationContext) {
        this.handlerChainClass = handlerChainClass;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final AutoAuthHandlerChain autoAuthHandlerChain = applicationContext.getBean(handlerChainClass);
        final String permissions = (String) request.getAttribute(PermissionOperate.PERMISSIONS);
        AuthHandlerUtil.handlerChain(autoAuthHandlerChain, applicationContext, request, permissions);
        return true;
    }
}
