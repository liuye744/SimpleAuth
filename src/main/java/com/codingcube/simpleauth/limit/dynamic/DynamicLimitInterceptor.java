package com.codingcube.simpleauth.limit.dynamic;

import com.codingcube.simpleauth.limit.util.LimitHandlerUtil;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author CodingCube<br>
 * The Dynamic HandlerChain Interceptor of SimpleAuth<br>
 * Implementing Dynamic Permission Configuration Functionality*
 */
public class DynamicLimitInterceptor implements HandlerInterceptor {
    private final RequestLimitItemProvider requestLimitItemProvider;
    private final ApplicationContext applicationContext;
    private final Log log;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public DynamicLimitInterceptor(RequestLimitItemProvider requestLimitItemProvider, ApplicationContext applicationContext, LogFactory logFactory) {
        this.requestLimitItemProvider = requestLimitItemProvider;
        this.applicationContext = applicationContext;
        this.log = logFactory.getLimitLog(this.getClass());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final List<RequestLimitItem> requestLimitItem = requestLimitItemProvider.getRequestLimitItem();
        LimitHandlerUtil.preHandlerRequestLimitItem(requestLimitItem,antPathMatcher, request, applicationContext, log, "dynamic limit");

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
