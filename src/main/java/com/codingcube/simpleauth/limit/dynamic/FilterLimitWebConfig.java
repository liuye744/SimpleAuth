package com.codingcube.simpleauth.limit.dynamic;


import com.codingcube.simpleauth.auth.dynamic.RequestAuthItemProvider;
import com.codingcube.simpleauth.auth.interceptor.DynamicAuthInterceptor;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author CodingCube<br>
 * Dynamic Permission Filtering Configuration Class*
 */
@Configuration
@ConditionalOnProperty("simple-auth.func.dynamic-limit")
public class FilterLimitWebConfig implements WebMvcConfigurer {
    @Resource
    RequestLimitItemProvider requestLimitItemProvider;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private LogFactory logFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DynamicLimitInterceptor(requestLimitItemProvider, applicationContext, logFactory)).addPathPatterns("/*").order(Integer.MAX_VALUE);
    }
}
