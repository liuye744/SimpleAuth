package com.codingcube.simpleauth.limit.util;

import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.exception.AccessIsRestrictedException;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LimitHandlerUtil {
    public static void preHandlerRequestLimitItem(List<RequestLimitItem> requestLimitItem,
                                                  AntPathMatcher antPathMatcher,
                                                  HttpServletRequest request,
                                                  ApplicationContext applicationContext,
                                                  Log log,
                                                  String source){
        final String requestURI = request.getRequestURI();
        for (RequestLimitItem limitItem : requestLimitItem) {
            final List<String> pathList = limitItem.getPath();
            for (String path : pathList) {
                if (antPathMatcher.match(path, requestURI)) {
                    //Create sign
                    SignStrategic signStrategic = AuthHandlerUtil.getBean(applicationContext, limitItem.getSignStrategic());
                    final String sign = signStrategic.sign(request, null);

                    //Verify that this request is recorded.
                    final SignStrategic itemStrategic = AuthHandlerUtil.getBean(applicationContext, limitItem.getItemStrategic());
                    final String item = itemStrategic.sign(request, null);

                    final Boolean addRecord = LimitInfoUtil.addRecord(item, sign, limitItem.getTimes(),
                            limitItem.getSeconds(), limitItem.getBan(), limitItem.getTokenLimit());
                    if (!addRecord) {
                        LogLimitFormat limitFormat = new LogLimitFormat(limitItem.getTimes(), limitItem.getSeconds(), limitItem.getBan(), item, limitItem.getSignStrategic(), sign,
                                source, true, limitItem.getEffectiveStrategic(), true, false);
                        log.debug(limitFormat.toString());
                        throw new AccessIsRestrictedException();
                    }
                }
            }
        }
    }

    public static void postHandlerRequestLimitItem(List<RequestLimitItem> requestLimitItem,
                                                   HttpServletRequest request,
                                                   AntPathMatcher antPathMatcher,
                                                   ApplicationContext applicationContext,
                                                   Log log,
                                                   Object result,
                                                   String source){
        final String requestURI = request.getRequestURI();
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
                    final Boolean isEffective = effectiveStrategicInstance.effective(request,null, result);
                    LogLimitFormat limitFormat = new LogLimitFormat(limitItem.getTimes(), limitItem.getSeconds(), limitItem.getBan(), item, limitItem.getSignStrategic(), sign,
                            source, true, limitItem.getEffectiveStrategic(), isEffective, true);
                    log.debug(limitFormat.toString());
                    if (!isEffective){
                        LimitInfoUtil.delRecord(item, sign);
                    }
                }
            }
        }
    }
}
