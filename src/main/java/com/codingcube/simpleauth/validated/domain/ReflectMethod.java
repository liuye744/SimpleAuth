package com.codingcube.simpleauth.validated.domain;

import java.lang.reflect.Method;

public class ReflectMethod {
    private final Object instance;
    private Method method;
    private Class<?> param;
    ReflectMethod next;

    public ReflectMethod(Object instance, Method method, Class<?> param, ReflectMethod next) {
        this.instance = instance;
        this.method = method;
        this.param = param;
        this.next = next;
    }

    public ReflectMethod(Object instance, Method method, Class<?> param) {
        this.instance = instance;
        this.method = method;
        this.param = param;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getParam() {
        return param;
    }

    public void setParam(Class<?> param) {
        this.param = param;
    }

    public ReflectMethod getNext() {
        return next;
    }

    public void setNext(ReflectMethod next) {
        this.next = next;
    }
}
