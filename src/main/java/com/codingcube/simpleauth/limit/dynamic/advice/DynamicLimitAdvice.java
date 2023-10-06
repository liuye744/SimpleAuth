package com.codingcube.simpleauth.limit.dynamic.advice;

import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItemProvider;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;
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
                    SignStrategic signStrategic = AuthHandlerUtil.getBean(applicationContext, limitItem.getSignStrategic());
                    final String sign = signStrategic.sign(request, null);

                    //Verify that this request is recorded.
                    final SignStrategic itemStrategic= AuthHandlerUtil.getBean(applicationContext, limitItem.getItemStrategic());
                    final String item = itemStrategic.sign(request, null);

                    EffectiveStrategic effectiveStrategicInstance = AuthHandlerUtil.getBean(applicationContext, limitItem.getEffectiveStrategic());
                    final Boolean isEffective = effectiveStrategicInstance.effective(request,null, o);
                    LogLimitFormat limitFormat = new LogLimitFormat(limitItem.getTimes(), limitItem.getSeconds(), limitItem.getBan(), item, limitItem.getSignStrategic(), sign,
                            "annotation limit", true, limitItem.getEffectiveStrategic(), isEffective, true);
                    log.debug(limitFormat.toString());
                    if (!isEffective){
                        LimitInfoUtil.delRecord(item, sign);
                    }
                }
            }
        }
        return o;
    }
}