package com.codingcube.domain;

import com.codingcube.handler.AutoAuthHandler;
import com.codingcube.handler.AutoAuthHandlerChain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CodingCube<br>
 * SimpleAuth Authentication Configuration item*
 */
public class RequestAuthItem {
    private List<String> path;
    private String permission;
    private Class<? extends AutoAuthHandler> handlerClass;
    private Class<? extends AutoAuthHandlerChain> handlerChainClass;

    public RequestAuthItem(List<String> path, String permission) {
        this.path = path;
        this.permission = permission;
    }

    public RequestAuthItem(String path, String permission) {
        List<String> list = new ArrayList<>(1);
        list.add(path);
        this.path = list;
        this.permission = permission;
    }

    public RequestAuthItem(Class<? extends AutoAuthHandlerChain> handlerChainClass, List<String> path, String permission) {
        this.path = path;
        this.permission = permission;
        this.handlerChainClass = handlerChainClass;
    }

    public RequestAuthItem(Class<? extends AutoAuthHandlerChain> handlerChainClass, String path, String permission) {
        List<String> list = new ArrayList<>(1);
        list.add(path);
        this.path = list;
        this.permission = permission;
        this.handlerChainClass = handlerChainClass;
    }


    public RequestAuthItem(List<String> path, String permission, Class<? extends AutoAuthHandler> handlerClass) {
        this.path = path;
        this.permission = permission;
        this.handlerClass = handlerClass;
    }

    public RequestAuthItem(String path, String permission, Class<? extends AutoAuthHandler> handlerClass) {
        List<String> list = new ArrayList<>(1);
        list.add(path);
        this.path = list;
        this.permission = permission;
        this.handlerClass = handlerClass;
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

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
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
