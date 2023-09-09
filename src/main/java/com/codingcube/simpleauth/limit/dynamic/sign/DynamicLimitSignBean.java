package com.codingcube.simpleauth.limit.dynamic.sign;

import com.codingcube.simpleauth.limit.dynamic.DynamicLimitConfig;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class DynamicLimitSignBean {

    @Bean
    @ConditionalOnMissingBean(DynamicLimitConfig.class)
    public DynamicLimitConfig filterAuthWebConfig(LogFactory logFactory){
        return new DynamicLimitConfig(logFactory);
    }
}
