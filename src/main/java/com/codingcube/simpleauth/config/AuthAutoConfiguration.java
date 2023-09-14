package com.codingcube.simpleauth.config;

import com.codingcube.simpleauth.properties.ClearProper;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author CodingCube<br>
 * Directory Scanning Configuration*
 */
@Configuration
@ComponentScan("com.codingcube.simpleauth.auth")
@ComponentScan("com.codingcube.simpleauth.limit")
@ComponentScan("com.codingcube.simpleauth.logging")
@ComponentScan("com.codingcube.simpleauth.properties")
@ConfigurationPropertiesScan("com.codingcube.simpleauth.properties")
public class AuthAutoConfiguration {
}
