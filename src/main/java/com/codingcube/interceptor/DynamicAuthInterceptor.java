package com.codingcube.interceptor;

import com.codingcube.domain.RequestAuthItem;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import com.codingcube.properties.RequestAuthItemProvider;
import com.codingcube.util.AuthHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author CodingCube<br>
 * The Dynamic HandlerChain Interceptor of SimpleAuth<br>
 * Implementing Dynamic Permission Configuration Functionality*
 */
public class DynamicAuthInterceptor implements HandlerInterceptor {
    private final RequestAuthItemProvider requestAuthItemProvider;
    private final ApplicationContext applicationContext;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public DynamicAuthInterceptor(RequestAuthItemProvider requestAuthItemProvider, ApplicationContext applicationContext) {
        this.requestAuthItemProvider = requestAuthItemProvider;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final List<RequestAuthItem> requestAuthItem = requestAuthItemProvider.getRequestAuthItem();
        for (RequestAuthItem authItem : requestAuthItem) {
            if (antPathMatcher.match(authItem.getPath(),request.getRequestURI())){
                final String permission = authItem.getPermission();
                final Class<? extends AutoAuthHandler> handlerClass = authItem.getHandlerClass();
                final Class<? extends AutoAuthHandlerChain> handlerChainClass = authItem.getHandlerChainClass();

                if (handlerClass != null){
                    //deal with Handler
                    final AutoAuthHandler authHandler = applicationContext.getBean(handlerClass);
                    authHandler.isAuthor(request, permission);
                }else if (handlerChainClass != null){
                    //deal with handlerChain
                    final AutoAuthHandlerChain authHandlerChain = applicationContext.getBean(handlerChainClass);
                    AuthHandlerUtil.handlerChain(authHandlerChain, applicationContext, request, permission);
                }else {
                    //parameter error
                    throw new InvalidParameterException("Need AutoAuthHandler or AutoAuthHandlerChain");
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
