package com.codingcube.simpleauth.autoconfig.domain;

import java.util.Map;

public class SimpleAuthConfig {
    Map<String, Handler> handlerMap;
    Map<String, Limit> limitMap;
    Map<String, Paths> pathsMap;
}
