package com.codingcube.simpleauth.limit.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>*
 * Effective Record Class*
 */
public abstract class EffectiveStrategic {
    public abstract Boolean effective(HttpServletRequest request, SimpleJoinPoint joinPoint, Object result);
}
