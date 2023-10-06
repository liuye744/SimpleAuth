package com.codingcube.simpleauth.auth.autoconfig;

import com.codingcube.simpleauth.auth.dynamic.RequestAuthItem;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class AutoConfigAuthInterceptor implements HandlerInterceptor{
    private final ApplicationContext applicationContext;
    private final Log log;
    private final List<RequestAuthItem> requestAuthItem = new ArrayList<>();
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AutoConfigAuthInterceptor(ApplicationContext applicationContext, LogFactory logFactory) {
        this.applicationContext = applicationContext;
        this.log = logFactory.getLog(this.getClass());
        AuthHandlerUtil.simpleAuthConfig.getHandlerMap().forEach(
                (key, value)-> requestAuthItem.add(new RequestAuthItem(value.getPaths().getPath(), value.getPaths().getPermission(), value.getHandlerClass()))
        );
        //TODO handlerChain
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthHandlerUtil.doRequestAuthItemList(requestAuthItem, antPathMatcher, request, applicationContext, log, "Profile configuration");

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
