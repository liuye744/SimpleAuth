package com.codingcube.simpleauth.limit.dynamic;

import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@ConditionalOnProperty("simple-auth.func.dynamic-limit")
public class DynamicLimitConfig implements WebMvcConfigurer {
    @Resource
    RequestLimitItemProvider requestLimitItemProvider;
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final Log log;

    public DynamicLimitConfig(LogFactory logFactory) {
        this.log = logFactory.getLimitLog(this.getClass());
    }

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
                            final Boolean effective = AuthHandlerUtil.getEffectiveStrategic(limitItem.getEffectiveStrategic(), request, null, null);
                            if (!effective){
                                LogLimitFormat limitFormat = new LogLimitFormat(times, seconds, ban, item,
                                        limitItem.getSignStrategic(),sign,"dynamic limit",false,
                                        limitItem.getEffectiveStrategic(),false, true);
                                log.debug(limitFormat.toString());
                                return true;
                            }
                            final Boolean addRecord = LimitInfoUtil.addRecord(item, sign, times, seconds, ban);
                            LogLimitFormat limitFormat = new LogLimitFormat(times, seconds, ban, item,
                                    limitItem.getSignStrategic(),sign,"dynamic limit",false,
                                    limitItem.getEffectiveStrategic(),true, addRecord);
                            log.debug(limitFormat.toString());
                            if (!addRecord){
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
