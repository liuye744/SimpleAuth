package com.codingcube.config;

import com.codingcube.properties.ClearProper;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author CodingCube<br>
 * Directory Scanning Configuration*
 */
@Configuration
@ComponentScan("com.codingcube.aspect")
@ComponentScan("com.codingcube.handler")
@ComponentScan("com.codingcube.logging")
@ConfigurationPropertiesScan("com.codingcube.properties")
public class AuthAutoConfiguration {

    private final ClearProper clearProper;

    public AuthAutoConfiguration(ClearProper clearProper) {
        this.clearProper = clearProper;
    }
}
