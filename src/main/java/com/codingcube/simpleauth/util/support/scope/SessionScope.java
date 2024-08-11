package com.codingcube.simpleauth.util.support.scope;

import com.codingcube.simpleauth.util.AuthHandlerUtil;
import com.codingcube.simpleauth.util.support.BeanDefinition;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Objects;

public class SessionScope extends Scope{
    @Override
    public <T> T initBean(BeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public <T> T getBean(BeanDefinition beanDefinition) {
        final Class<?> clazz = beanDefinition.getClazz();
        final HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession(true);
        final String sessionBeanKey = AuthHandlerUtil.requestBeanKey(clazz);
        final Object sessionAttribute = session.getAttribute(sessionBeanKey);
        if (sessionAttribute != null) {
            return (T) sessionAttribute;
        }

        synchronized (session){
            final Object tempAttribute = session.getAttribute(sessionBeanKey);
            if(tempAttribute != null){
                return (T) tempAttribute;
            }
            final Object objInstance = ReflectUtils.newInstance(clazz);
            session.setAttribute(sessionBeanKey, objInstance);
            return (T) objInstance;
        }

    }
}
