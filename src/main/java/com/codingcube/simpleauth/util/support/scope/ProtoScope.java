package com.codingcube.simpleauth.util.support.scope;

import com.codingcube.simpleauth.util.support.BeanDefinition;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.InvocationTargetException;

public class ProtoScope extends Scope{
    @Override
    public <T> T initBean(BeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public <T> T getBean(BeanDefinition beanDefinition) {
        final Class<?> clazz = beanDefinition.getClazz();
        return (T) ReflectUtils.newInstance(clazz);
    }
}
