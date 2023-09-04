package com.codingcube.interceptor;

import com.codingcube.exception.PermissionsException;
import com.codingcube.handler.AutoAuthHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class AutoAuthInterceptor implements HandlerInterceptor {
    private final Class<? extends AutoAuthHandler> handlerClass;
    private final ApplicationContext applicationContext;

    public AutoAuthInterceptor(Class<? extends AutoAuthHandler> handlerClass, ApplicationContext applicationContext) {
        this.handlerClass = handlerClass;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final AutoAuthHandler autoAuthHandler = applicationContext.getBean(handlerClass);
        final ArrayList<String> permissions = autoAuthHandler.getPermissions();
        String permissionString = permissions==null?"":permissions.toString();

        final boolean author = autoAuthHandler.isAuthor(request, permissionString);
        if (!author){
            throw new PermissionsException("lack of permissions");
        }
        return true;
    }
}
