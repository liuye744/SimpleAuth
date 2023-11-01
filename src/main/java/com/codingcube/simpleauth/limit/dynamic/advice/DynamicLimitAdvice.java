package com.codingcube.simpleauth.limit.dynamic.advice;

import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItemProvider;
import com.codingcube.simpleauth.limit.util.LimitHandlerUtil;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ConditionalOnProperty("simple-auth.func.dynamic-limit")
public class DynamicLimitAdvice implements ResponseBodyAdvice<Object> {
    @Resource
    private RequestLimitItemProvider requestLimitItemProvider;
    @Resource
    private ApplicationContext applicationContext;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Log log;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    public DynamicLimitAdvice(LogFactory logFactory) {
        this.log = logFactory.getLimitLog(this.getClass());
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return o;
        }
        final HttpServletRequest request = requestAttributes.getRequest();
        final List<RequestLimitItem> requestLimitItem = requestLimitItemProvider.getRequestLimitItem();
        LimitHandlerUtil.postHandlerRequestLimitItem(requestLimitItem, request,
                antPathMatcher, applicationContext, log, o, "dynamic limit");
        return o;
    }
}