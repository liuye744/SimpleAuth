package com.codingcube.simpleauth.limit.dynamic;

import com.codingcube.simpleauth.auth.dynamic.RequestAuthItem;
import com.codingcube.simpleauth.auth.dynamic.RequestAuthItemProvider;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.properties.FunctionProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
        this.log = logFactory.getLog(this.getClass());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String requestURI = request.getRequestURI();
        final List<RequestLimitItem> requestLimitItem = requestLimitItemProvider.getRequestLimitItem();
        for (RequestLimitItem limitItem : requestLimitItem) {
            final List<String> pathList = limitItem.getPath();
            for (String path : pathList) {
                if (antPathMatcher.match(path, requestURI)){
                    //Create sign
                    SignStrategic signStrategic = AuthHandlerUtil.getBean(applicationContext, limitItem.getSignStrategic());
                    final String sign = signStrategic.sign(request, null);

                    //Verify that this request is recorded.
                    final SignStrategic itemStrategic= AuthHandlerUtil.getBean(applicationContext, limitItem.getItemStrategic());
                    final String item = itemStrategic.sign(request, null);

                    final Boolean addRecord = LimitInfoUtil.addRecord(item, sign, limitItem.getTimes(),
                            limitItem.getSeconds(), limitItem.getBan(), limitItem.getTokenLimit());
                    if (!addRecord){
                        LogLimitFormat limitFormat = new LogLimitFormat(limitItem.getTimes(), limitItem.getSeconds(), limitItem.getBan(), item, limitItem.getSignStrategic(), sign,
                                "annotation limit", true, limitItem.getEffectiveStrategic(), true, false);
                        log.debug(limitFormat.toString());
                        throw new AccessIsRestrictedException();
                    }
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
