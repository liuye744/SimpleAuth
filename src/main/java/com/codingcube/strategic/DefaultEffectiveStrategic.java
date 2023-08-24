package com.codingcube.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

public class DefaultEffectiveStrategic extends EffectiveStrategic{

    @Override
    public Boolean effective(HttpServletRequest request, ProceedingJoinPoint joinPoint, Object result) {
        return true;
    }
}
