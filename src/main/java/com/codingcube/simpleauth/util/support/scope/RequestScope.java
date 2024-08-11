package com.codingcube.simpleauth.util.support.scope;

import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.codingcube.simpleauth.util.support.BeanDefinition;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RequestScope extends Scope{
    @Override
    public <T> T initBean(BeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public <T> T getBean(BeanDefinition beanDefinition) {
        final Class<?> clazz = beanDefinition.getClazz();
        //request
        final HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        final String requestBeanKey = AuthHandlerUtil.requestBeanKey(clazz);
        final Object attribute = request.getAttribute(requestBeanKey);
        if (attribute != null) {
            return (T) attribute;
        }
        synchronized (request){
            final Object syncAttribute = request.getAttribute(requestBeanKey);
            if (syncAttribute != null) {
                return (T) syncAttribute;
            }

            final Object objInstance = ReflectUtils.newInstance(clazz);
            request.setAttribute(requestBeanKey, objInstance);
            return (T) objInstance;
        }
    }
}
