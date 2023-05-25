package com.codingcube.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;




@ConfigurationProperties(prefix = "simple-auth")
public class Proper {
    private boolean blacklist = false;
    private boolean whitelist = false;
}
