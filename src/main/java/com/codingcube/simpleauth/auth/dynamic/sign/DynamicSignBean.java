package com.codingcube.simpleauth.auth.dynamic.sign;

import com.codingcube.simpleauth.auth.dynamic.FilterAuthWebConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

public class DynamicSignBean {
    @Bean
    @ConditionalOnMissingBean(FilterAuthWebConfig.class)
    public FilterAuthWebConfig filterAuthWebConfig(){
        return new FilterAuthWebConfig();
    }
}
