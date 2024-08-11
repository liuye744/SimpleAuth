package com.codingcube.simpleauth.util.support.scope;

import com.codingcube.simpleauth.util.support.BeanDefinition;

/**
 * @author dhc
 */
public abstract class Scope {

    /**
     * init JavaBean
     * @param beanDefinition beanDefinition
     * @return JavaBean
     */
    public abstract <T> T initBean(BeanDefinition beanDefinition);

    /**
     * get JavaBean
     * @param beanDefinition beanDefinition
     * @return JavaBean
     */
    public abstract <T> T getBean(BeanDefinition beanDefinition);
}
