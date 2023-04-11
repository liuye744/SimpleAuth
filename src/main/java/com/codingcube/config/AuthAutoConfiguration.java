package com.codingcube.config;

import com.codingcube.properties.Proper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(Proper.class)
@ComponentScan("com.codingcube.aspect")
@ComponentScan("com.codingcube.handler")
public class AuthAutoConfiguration {

    private final Proper proper;

    public AuthAutoConfiguration(Proper proper) {
        this.proper = proper;
    }
}
