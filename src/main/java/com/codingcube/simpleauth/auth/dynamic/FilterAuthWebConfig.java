package com.codingcube.simpleauth.auth.dynamic;


import com.codingcube.simpleauth.auth.dynamic.sign.DynamicSignBean;
import com.codingcube.simpleauth.auth.interceptor.DynamicAuthInterceptor;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Autowired;

/**
 * @author CodingCube<br>
 * Dynamic Permission Filtering Configuration Class*
 */
@Configuration
@ConditionalOnProperty("simple-auth.func.dynamic-auth")
public class FilterAuthWebConfig implements WebMvcConfigurer {
    @Autowired
    RequestAuthItemProvider requestAuthItemProvider;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LogFactory logFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DynamicAuthInterceptor(requestAuthItemProvider, applicationContext, logFactory)).addPathPatterns("/*").order(Integer.MAX_VALUE);
    }
}
