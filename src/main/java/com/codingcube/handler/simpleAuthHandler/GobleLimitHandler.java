package com.codingcube.handler.simpleAuthHandler;

import com.codingcube.annotation.IsLimit;
import com.codingcube.domain.RequestLimitItem;
import com.codingcube.exception.AccessIsRestrictedException;
import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.properties.LimitInfoUtil;
import com.codingcube.properties.RequestLimitItemProvider;
import com.codingcube.strategic.SignStrategic;
import com.codingcube.util.AuthHandlerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class GobleLimitHandler extends AutoAuthHandler {
    @Resource
    RequestLimitItemProvider requestLimitItemProvider;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean isAuthor(HttpServletRequest request, String permission) {
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
}
