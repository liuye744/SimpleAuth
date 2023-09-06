package com.codingcube.domain;

import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;

public class RequestAuthItem {
    String path;
    String permission;
    Class<? extends AutoAuthHandler> handlerClass;
    Class<? extends AutoAuthHandlerChain> handlerChainClass;

    public RequestAuthItem(String path, String permission, Class<? extends AutoAuthHandler> handlerClass) {
        this.path = path;
        this.permission = permission;
        this.handlerClass = handlerClass;
    }

    public RequestAuthItem(Class<? extends AutoAuthHandlerChain> handlerChainClass, String path, String permission) {
        this.path = path;
        this.permission = permission;
        this.handlerChainClass = handlerChainClass;
    }

    public RequestAuthItem(String path, String permission) {
        this.path = path;
        this.permission = permission;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Class<? extends AutoAuthHandler> getHandlerClass() {
        return handlerClass;
    }

    public void setHandlerClass(Class<? extends AutoAuthHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public Class<? extends AutoAuthHandlerChain> getHandlerChainClass() {
        return handlerChainClass;
    }

    public void setHandlerChainClass(Class<? extends AutoAuthHandlerChain> handlerChainClass) {
        this.handlerChainClass = handlerChainClass;
    }

    @Override
    public String toString() {
        return "RequestAuthItem{" + "path='" + path + '\'' +
                ", permission='" + permission + '\'' +
                ", handlerClass=" + handlerClass +
                ", handlerChainClass=" + handlerChainClass +
                '}';
    }
}
