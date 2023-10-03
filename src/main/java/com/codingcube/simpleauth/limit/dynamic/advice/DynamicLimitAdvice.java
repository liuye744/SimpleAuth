package com.codingcube.simpleauth.limit.dynamic.advice;

import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItemProvider;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.properties.FunctionProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
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
        final String requestURI = request.getRequestURI();
        final List<RequestLimitItem> requestLimitItem = requestLimitItemProvider.getRequestLimitItem();
        for (RequestLimitItem limitItem : requestLimitItem) {
            final List<String> pathList = limitItem.getPath();
            for (String path : pathList) {
                if (antPathMatcher.match(path, requestURI)){
                    //初始化参数
                    final String item = AuthHandlerUtil.getSignStrategic(limitItem.getItemStrategic(), request, null, applicationContext);
                    final String sign = AuthHandlerUtil.getSignStrategic(limitItem.getSignStrategic(), request, null, applicationContext);
                    final Integer times = limitItem.getTimes();
                    final Integer ban = limitItem.getBan();
                    final Integer seconds = limitItem.getSeconds();
                    final Boolean effective = AuthHandlerUtil.getEffectiveStrategic(limitItem.getEffectiveStrategic(), request, null, o, applicationContext);
                    if (!effective){
                        LogLimitFormat limitFormat = new LogLimitFormat(times, seconds, ban, item,
                                limitItem.getSignStrategic(),sign,"dynamic limit",true,
                                limitItem.getEffectiveStrategic(),false, true);
                        log.debug(limitFormat.toString());
                        return o;
                    }
                    final Boolean addRecord = LimitInfoUtil.addRecord(item, sign, times, seconds, ban, FunctionProper.getTokenLimitClass());
                    LogLimitFormat limitFormat = new LogLimitFormat(times, seconds, ban, item,
                            limitItem.getSignStrategic(),sign,"dynamic limit",true,
                            limitItem.getEffectiveStrategic(),true, addRecord);
                    log.debug(limitFormat.toString());
                    if (!addRecord){
                        throw new AccessIsRestrictedException();
                    }else {
                        return o;
                    }
                }
            }
        }
        return o;
    }
}