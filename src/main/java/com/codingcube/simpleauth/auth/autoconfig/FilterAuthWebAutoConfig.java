package com.codingcube.simpleauth.auth.autoconfig;

import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.Resource;

@Configuration
public class FilterAuthWebAutoConfig implements WebMvcConfigurer {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private LogFactory logFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (AuthHandlerUtil.simpleAuthConfig != null){
            if (AuthHandlerUtil.simpleAuthConfig.getHandlerMap().size() != 0) {
                registry.addInterceptor(new AutoConfigAuthInterceptor(applicationContext, logFactory)).addPathPatterns("/*").order(Integer.MAX_VALUE);
            }
        }
    }
}
