package com.codingcube.simpleauth.autoconfig;

import com.codingcube.simpleauth.autoconfig.domain.SimpleAuthConfig;

import java.util.List;


public interface Config2SimpleAuthObject {
    void initAttr(SimpleAuthConfig simpleAuthConfig);
    void initConfig();
    List<String> getConfig();
    void initPaths();
    void initHandler();
    void initHandlerChain();
    void initLimit();

    default void init(){
        initConfig();
        initPaths();
        initHandler();
        initLimit();
        initHandlerChain();
    }
}
