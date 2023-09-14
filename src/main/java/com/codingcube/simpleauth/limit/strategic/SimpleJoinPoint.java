package com.codingcube.simpleauth.limit.strategic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public class SimpleJoinPoint implements JoinPoint {
    private final ProceedingJoinPoint joinPoint;

    public SimpleJoinPoint(ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    @Override
    public String toShortString() {
        return joinPoint.toShortString();
    }

    @Override
    public String toLongString() {
        return joinPoint.toLongString();
    }

    @Override
    public Object getThis() {
        return joinPoint.getThis();
    }

    @Override
    public Object getTarget() {
        return joinPoint.getTarget();
    }

    @Override
    public Object[] getArgs() {
        return joinPoint.getArgs();
    }

    @Override
    public Signature getSignature() {
        return joinPoint.getSignature();
    }

    @Override
    public SourceLocation getSourceLocation() {
        return joinPoint.getSourceLocation();
    }

    @Override
    public String getKind() {
        return joinPoint.getKind();
    }

    @Override
    public StaticPart getStaticPart() {
        return joinPoint.getStaticPart();
    }
}
