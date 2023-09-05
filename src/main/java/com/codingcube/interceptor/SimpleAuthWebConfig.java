package com.codingcube.interceptor;

import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;
import com.codingcube.handler.DefaultAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public abstract class SimpleAuthWebConfig implements WebMvcConfigurer {
    @Resource
    private ApplicationContext applicationContext;
    InterceptorRegistry registry;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        this.registry = registry;
        addAuthHandlers();
    }

    public InterceptorRegistration addAuthHandler(Class<? extends AutoAuthHandler> autoAuthHandler){
        return registry.addInterceptor(new AutoAuthInterceptor(autoAuthHandler, applicationContext));
    }

    public InterceptorRegistration addAuthHandler(String handlerBeanName){
        return registry.addInterceptor(new AutoAuthInterceptor(handlerBeanName, applicationContext));
    }

    public InterceptorRegistration addAuthHandlerChain(Class<? extends AutoAuthHandlerChain> autoAuthHandlerChain){
        return registry.addInterceptor(new AutoAuthChainInterceptor(autoAuthHandlerChain, applicationContext));
    }

    public abstract void addAuthHandlers();

}
