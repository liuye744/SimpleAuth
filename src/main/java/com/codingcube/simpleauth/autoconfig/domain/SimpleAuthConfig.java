package com.codingcube.simpleauth.autoconfig.domain;

import java.util.List;
import java.util.Map;

public class SimpleAuthConfig {
    Map<String, Handler> handlerMap;
    Map<String, Limit> limitMap;
    Map<String, HandlerChain> handlerChainMap;
    Map<String, Paths> pathsMap;

    public SimpleAuthConfig(Map<String, Handler> handlerMap, Map<String, Limit> limitMap, Map<String, Paths> pathsMap, Map<String, HandlerChain> handlerChainMap) {
        this.handlerMap = handlerMap;
        this.limitMap = limitMap;
        this.pathsMap = pathsMap;
        this.handlerChainMap = handlerChainMap;
    }

    public Map<String, Handler> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(Map<String, Handler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public Map<String, Limit> getLimitMap() {
        return limitMap;
    }

    public void setLimitMap(Map<String, Limit> limitMap) {
        this.limitMap = limitMap;
    }

    public Map<String, Paths> getPathsMap() {
        return pathsMap;
    }

    public void setPathsMap(Map<String, Paths> pathsMap) {
        this.pathsMap = pathsMap;
    }

    public Map<String, HandlerChain> getHandlerChainMap() {
        return handlerChainMap;
    }

    public void setHandlerChainMap(Map<String, HandlerChain> handlerChainMap) {
        this.handlerChainMap = handlerChainMap;
    }
}
