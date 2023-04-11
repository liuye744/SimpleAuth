package com.codingcube.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "simple-auth")
@ToString
public class Proper {
    private boolean blacklist = false;
    private boolean whitelist = false;
}
