package com.codingcube.simpleauth.auth.strategic;

import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>*
 * Default sign Generation Strategy Class*
 */
public class DefaultSignStrategic extends SignStrategic{
    @Override
    public String sign(HttpServletRequest request, SimpleJoinPoint joinPoint) {
        return request.getRemoteAddr();
    }
}
