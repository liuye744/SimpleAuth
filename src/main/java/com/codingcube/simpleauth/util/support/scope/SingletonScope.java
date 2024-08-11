package com.codingcube.simpleauth.util.support.scope;

import com.codingcube.simpleauth.autoconfig.execption.ConfigurationParseException;
import com.codingcube.simpleauth.util.support.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dhc
 */
public class SingletonScope extends Scope{
    public static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>(16);

    @Override
    public <T> T initBean(BeanDefinition beanDefinition) {
        final Class<?> clazz = beanDefinition.getClazz();
        final Object obj;
        try {
            obj = beanMap.get(clazz.getName());
            if (obj == null){
                final Object objInstance = clazz.getConstructor().newInstance();
                beanMap.put(clazz.getName(), objInstance);
            }
        } catch ( NoSuchMethodException e) {
            throw new ConfigurationParseException(clazz.getName() + " Requires a parameterless constructor, or remove the related configuration.");
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException e){
            throw new ConfigurationParseException(e);
        }
        return (T) obj;
    }

    @Override
    public <T> T getBean(BeanDefinition beanDefinition) {
        final Class<T> clazz = (Class<T>) beanDefinition.getClazz();
        final String beanName = beanDefinition.getBeanName();
        final Object obj = beanMap.get(beanName);
        if (obj != null){
            return clazz.cast(obj);
        }
        synchronized (beanMap){
            T objInstance = null;
            try {
                objInstance = clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            if(objInstance != null){
                beanMap.put(clazz.getName(), objInstance);
            }
            return objInstance;
        }
    }
}
