package com.codingcube.simpleauth.validated.util;

@FunctionalInterface
public interface BooleanGenericCompute<T> {
    boolean run(T t);
}
