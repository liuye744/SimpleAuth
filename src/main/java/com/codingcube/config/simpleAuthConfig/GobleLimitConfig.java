package com.codingcube.config.simpleAuthConfig;

import com.codingcube.domain.RequestLimitItem;
import com.codingcube.exception.AccessIsRestrictedException;
import com.codingcube.properties.LimitInfoUtil;
import com.codingcube.properties.RequestLimitItemProvider;
import com.codingcube.util.AuthHandlerUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class GobleLimitConfig implements WebMvcConfigurer {
    @Resource
    RequestLimitItemProvider requestLimitItemProvider;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                final String requestURI = request.getRequestURI();
                final List<RequestLimitItem> requestLimitItem = requestLimitItemProvider.getRequestLimitItem();
                for (RequestLimitItem limitItem : requestLimitItem) {
                    final List<String> pathList = limitItem.getPath();
                    for (String path : pathList) {
                        if (antPathMatcher.match(path, requestURI)){
                            //初始化参数
                            final String item = AuthHandlerUtil.getSignStrategic(limitItem.getItemStrategic(), request, null);
                            final String sign = AuthHandlerUtil.getSignStrategic(limitItem.getSignStrategic(), request, null);
                            final Integer times = limitItem.getTimes();
                            final Integer ban = limitItem.getBan();
                            final Integer seconds = limitItem.getSeconds();
                            if (!LimitInfoUtil.addRecord(item, sign, times, seconds, ban)){
                                throw new AccessIsRestrictedException();
                            }else {
                                return true;
                            }
                        }
                    }
                }
                return true;
            }
        }).addPathPatterns("/*").order(Integer.MIN_VALUE);

    }
}
