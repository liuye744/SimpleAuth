package com.codingcube.simpleauth.config;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.auth.interceptor.AutoAuthChainInterceptor;
import com.codingcube.simpleauth.auth.interceptor.AutoAuthInterceptor;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author CodingCube<br>
 * SimpleAuth Interceptor Configuration Class*
 */
@Configuration
public abstract class SimpleAuthWebConfig implements WebMvcConfigurer {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private LogFactory logFactory;
    InterceptorRegistry registry;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        this.registry = registry;
        addAuthHandlers();
    }

    /**
     * add AuthHandler Class as Interceptor*
     * @param autoAuthHandler autoAuthHandler Class
     * @return InterceptorRegistration
     */
    public InterceptorRegistration addAuthHandler(Class<? extends AutoAuthHandler> autoAuthHandler){
        return registry.addInterceptor(new AutoAuthInterceptor(autoAuthHandler, applicationContext, logFactory));
    }

    /**
     * add AuthHandler BeanName as Interceptor*
     * @param handlerBeanName handler BeanName
     * @return InterceptorRegistration
     */
    public InterceptorRegistration addAuthHandler(String handlerBeanName){
        return registry.addInterceptor(new AutoAuthInterceptor(handlerBeanName, applicationContext));
    }

    /**
     * add AuthHandlerChain Class as Interceptor*
     * @param autoAuthHandlerChain AutoAuthHandlerChain class
     * @return InterceptorRegistration
     */
    public InterceptorRegistration addAuthHandlerChain(Class<? extends AutoAuthHandlerChain> autoAuthHandlerChain){
        return registry.addInterceptor(new AutoAuthChainInterceptor(autoAuthHandlerChain, applicationContext, logFactory));
    }

    public abstract void addAuthHandlers();

}
