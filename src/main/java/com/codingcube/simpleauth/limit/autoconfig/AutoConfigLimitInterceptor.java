package com.codingcube.simpleauth.limit.autoconfig;

import com.codingcube.simpleauth.autoconfig.domain.Limit;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItemProvider;
import com.codingcube.simpleauth.limit.util.LimitHandlerUtil;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AutoConfigLimitInterceptor implements HandlerInterceptor {
    final List<RequestLimitItem> requestLimitItem = new ArrayList<>();
    private final ApplicationContext applicationContext;
    private final Log log;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public AutoConfigLimitInterceptor(ApplicationContext applicationContext, LogFactory logFactory) {
        this.applicationContext = applicationContext;
        this.log = logFactory.getLimitLog(this.getClass());
        final Map<String, Limit> limitMap = AuthHandlerUtil.simpleAuthConfig.getLimitMap();
        limitMap.forEach(
                (key, value)-> requestLimitItem.add(
                        new RequestLimitItem(value.getPaths().getPath(),
                                value.getTimes(),
                                value.getSeconds(),
                                value.getBan(),
                                value.getItemStrategicClass(),
                                value.getSignStrategicClass(),
                                value.getEffectiveStrategicClass(),
                                value.getTokenLimitClass(),
                                value.getRejectedClass()
                        )
                )
        );
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LimitHandlerUtil.preHandlerRequestLimitItem(requestLimitItem,antPathMatcher, request, applicationContext, log, "Profile configuration limit");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
