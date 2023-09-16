package com.codingcube.simpleauth.security.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContentSecurityPolicyInterceptor implements HandlerInterceptor {
    private final String contentSecurityPolicy;

    public ContentSecurityPolicyInterceptor(String contentSecurityPolicy) {
        this.contentSecurityPolicy = contentSecurityPolicy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        response.setHeader("Content-Security-Policy", contentSecurityPolicy);
        return true;
    }
}
