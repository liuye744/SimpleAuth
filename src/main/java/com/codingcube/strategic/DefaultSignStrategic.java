package com.codingcube.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

public class DefaultSignStrategic extends SignStrategic{
    @Override
    public String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        return request.getRemoteAddr();
    }
}
