package com.codingcube.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "simple-auth.goble-limit")
public class GobleLimitProper {
    private boolean enable = false;
}
