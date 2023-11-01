package com.codingcube.simpleauth.limit.autoconfig;

import com.codingcube.simpleauth.auth.autoconfig.AutoConfigAuthInterceptor;
import com.codingcube.simpleauth.logging.LogFactory;
import com.codingcube.simpleauth.properties.Bean2Static;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class FilterLimitWebAutoConfig  implements WebMvcConfigurer {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private LogFactory logFactory;
    @Resource
    private Bean2Static bean2Static;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (AuthHandlerUtil.simpleAuthConfig != null){
            if (AuthHandlerUtil.simpleAuthConfig.getLimitMap().size() != 0){
                registry.addInterceptor(new AutoConfigLimitInterceptor(applicationContext, logFactory)).addPathPatterns("/*").order(Integer.MAX_VALUE);

            }
        }
    }

    @Bean
    @ConditionalOnMissingBean(AutoConfigLimitAdvice.class)
    public AutoConfigLimitAdvice autoConfigLimitAdvice(LogFactory logFactory){
        return new AutoConfigLimitAdvice(logFactory, bean2Static);
    }
}
