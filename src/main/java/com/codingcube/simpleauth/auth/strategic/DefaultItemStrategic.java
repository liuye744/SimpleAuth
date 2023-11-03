package com.codingcube.simpleauth.auth.strategic;

import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

public class DefaultItemStrategic implements SignStrategic{
    @Override
    public String sign(HttpServletRequest request, SimpleJoinPoint joinPoint) {
        return request.getRequestURI();
    }
}
