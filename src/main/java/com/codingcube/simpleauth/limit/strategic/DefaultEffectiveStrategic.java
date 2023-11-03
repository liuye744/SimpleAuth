package com.codingcube.simpleauth.limit.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CodingCube<br>*
 * Default Effective Record Class*
 */
public class DefaultEffectiveStrategic implements EffectiveStrategic {

    @Override
    public Boolean effective(HttpServletRequest request, SimpleJoinPoint joinPoint, Object result) {
        return true;
    }
}
