package com.codingcube.simpleauth.security.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddXFrameOptions implements HandlerInterceptor {
    private final String xFrameOptions;

    public AddXFrameOptions(String xFrameOptions) {
        this.xFrameOptions = xFrameOptions;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        response.setHeader("X-Frame-Options", xFrameOptions);
        return true;
    }
}
