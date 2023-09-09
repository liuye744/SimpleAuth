package com.codingcube.simpleauth.auth.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>*
 * sign Generation Strategy*
 */
public abstract class SignStrategic {
    public abstract String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint);
}
