package com.codingcube.simpleauth.auth.strategic;

import com.codingcube.simpleauth.limit.strategic.SimpleJoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>*
 * sign Generation Strategy*
 */
public interface SignStrategic {
    String sign(HttpServletRequest request, SimpleJoinPoint joinPoint);
}
