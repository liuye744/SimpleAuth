package com.codingcube.simpleauth.auth.interceptor;

import com.codingcube.simpleauth.auth.dynamic.RequestAuthItem;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.auth.dynamic.RequestAuthItemProvider;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

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
    private final Log log;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public DynamicAuthInterceptor(RequestAuthItemProvider requestAuthItemProvider, ApplicationContext applicationContext, LogFactory logFactory) {
        this.requestAuthItemProvider = requestAuthItemProvider;
        this.applicationContext = applicationContext;
        this.log = logFactory.getLog(this.getClass());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final List<RequestAuthItem> requestAuthItem = requestAuthItemProvider.getRequestAuthItem();
        for (RequestAuthItem authItem : requestAuthItem) {
            final List<String> paths = authItem.getPath();
            for (String path: paths){
                if (antPathMatcher.match(path,request.getRequestURI())){
                    final String permission = authItem.getPermission();
                    final Class<? extends AutoAuthHandler> handlerClass = authItem.getHandlerClass();
                    final Class<? extends AutoAuthHandlerChain> handlerChainClass = authItem.getHandlerChainClass();

                    if (handlerClass != null){
                        //deal with Handler
                        AuthHandlerUtil.handler(request, permission, handlerClass, applicationContext, log, "Dynamic Permission Configuration");
                    }else if (handlerChainClass != null){
                        //deal with handlerChain
                        final AutoAuthHandlerChain authHandlerChain = AuthHandlerUtil.getBean(applicationContext, handlerChainClass);
                        AuthHandlerUtil.handlerChain(authHandlerChain, applicationContext, request, permission, log, "Dynamic Permission Configuration");
                    }else {
                        //parameter error
                        throw new InvalidParameterException("Need AutoAuthHandler or AutoAuthHandlerChain");
                    }
                }

            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
