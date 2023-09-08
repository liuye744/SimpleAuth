package com.codingcube.config;

import com.codingcube.properties.Proper;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author CodingCube<br>
 * Directory Scanning Configuration*
 */
@Configuration
@EnableConfigurationProperties(Proper.class)
@ComponentScan("com.codingcube.aspect")
@ComponentScan("com.codingcube.handler")
@ComponentScan("com.codingcube.logging")
@ConfigurationPropertiesScan("com.codingcube.properties")
public class AuthAutoConfiguration {

    private final Proper proper;

    public AuthAutoConfiguration(Proper proper) {
        this.proper = proper;
    }
}
