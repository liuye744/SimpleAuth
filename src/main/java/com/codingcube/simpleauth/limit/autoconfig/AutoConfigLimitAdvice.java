package com.codingcube.simpleauth.limit.autoconfig;

import com.codingcube.simpleauth.autoconfig.domain.Limit;
import com.codingcube.simpleauth.autoconfig.domain.SimpleAuthConfig;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.util.LimitHandlerUtil;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class AutoConfigLimitAdvice implements ResponseBodyAdvice<Object> {
    private final List<RequestLimitItem> requestLimitItem = new ArrayList<>();
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Resource
    private ApplicationContext applicationContext;
    private final Log log;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    public AutoConfigLimitAdvice(LogFactory logFactory) {
        final SimpleAuthConfig simpleAuthConfig = AuthHandlerUtil.simpleAuthConfig;
        if (simpleAuthConfig != null && simpleAuthConfig.getLimitMap().size()!=0){
            final Map<String, Limit> limitMap = simpleAuthConfig.getLimitMap();
            limitMap.forEach(
                    (key, value)-> requestLimitItem.add(
                            new RequestLimitItem(value.getPaths().getPath(),
                            value.getTimes(),
                            value.getSeconds(),
                            value.getBan(),
                            value.getItemStrategicClass(),
                            value.getSignStrategicClass(),
                            value.getEffectiveStrategicClass(),
                            value.getTokenLimitClass())
                    )
            );
        }
        this.log = logFactory.getLimitLog(this.getClass());
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        LimitHandlerUtil.postHandlerRequestLimitItem(requestLimitItem, request,
                antPathMatcher, applicationContext, log, o, "Profile configuration limit");
        return o;
    }


}
