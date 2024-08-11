package com.codingcube.simpleauth.util.support;

import com.codingcube.simpleauth.util.support.scope.ScopeType;

/**
 * @author dhc
 */
public class BeanDefinition {
    ScopeType type;
    String beanName;
    Class<?> clazz;

    public BeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        type = ScopeType.SINGLETON;
        this.beanName = clazz.getName();
    }

    public BeanDefinition(Class<?> clazz, ScopeType type) {
        this.clazz = clazz;
        this.beanName = clazz.getName();
        this.type = type;
    }

    public BeanDefinition(Class<?> clazz, String beanName, ScopeType type) {
        this.type = type;
        this.beanName = beanName;
        this.clazz = clazz;
    }

    public ScopeType getType() {
        return type;
    }

    public void setType(ScopeType type) {
        this.type = type;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
