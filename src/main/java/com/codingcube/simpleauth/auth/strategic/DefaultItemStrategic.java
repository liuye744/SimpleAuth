package com.codingcube.simpleauth.auth.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

public class DefaultItemStrategic extends SignStrategic{
    @Override
    public String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        return request.getRequestURI();
    }
}
