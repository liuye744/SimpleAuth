package com.codingcube.simpleauth.limit.strategic.sign;

import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import javax.servlet.http.HttpServletRequest;

public class DiffParameterSign extends SignStrategic {
    @Override
    public String sign(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        final Signature signature = joinPoint.getSignature();
        StringBuilder sb = new StringBuilder();
        sb.append(signature);
        for (Object arg : args) {
            sb.append("$");
            sb.append(arg.toString());
        }
        return sb.toString();
    }
}
