package com.codingcube.strategic;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

public abstract class SignStrategic {
    public abstract String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint);
}
