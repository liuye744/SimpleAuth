package com.codingcube.simpleauth.auth.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>*
 * Default sign Generation Strategy Class*
 */
public class DefaultSignStrategic extends SignStrategic{
    @Override
    public String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        return request.getRemoteAddr();
    }
}
