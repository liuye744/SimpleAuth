package com.codingcube.simpleauth.limit.dynamic.sign;

import com.codingcube.simpleauth.limit.dynamic.advice.DynamicLimitAdvice;
import com.codingcube.simpleauth.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class DynamicLimitSignBean {

    @Bean
    @ConditionalOnMissingBean(DynamicLimitAdvice.class)
    public DynamicLimitAdvice filterAuthWebConfig(LogFactory logFactory){
        return new DynamicLimitAdvice(logFactory);
    }
}
