package com.codingcube.simpleauth.auth.autoconfig;

import com.codingcube.simpleauth.autoconfig.domain.SimpleAuthConfig;
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
        final SimpleAuthConfig simpleAuthConfig = AuthHandlerUtil.simpleAuthConfig;
        if (simpleAuthConfig != null){
            if (simpleAuthConfig.getHandlerMap().size() != 0
                    ||
                simpleAuthConfig.getHandlerChainMap().size() != 0) {
                registry.addInterceptor(new AutoConfigAuthInterceptor(applicationContext, logFactory)).addPathPatterns("/*").order(Integer.MAX_VALUE);
            }
        }
    }
}
