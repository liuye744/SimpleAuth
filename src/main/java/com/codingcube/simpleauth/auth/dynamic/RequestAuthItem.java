package com.codingcube.simpleauth.auth.dynamic;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.handler.AutoAuthHandlerChain;
import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.auth.strategic.DefaultAuthRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;

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
    private AutoAuthHandlerChain handlerChain;
    private AutoAuthHandler handler;
    private Class<? extends AuthRejectedStratagem> rejected = DefaultAuthRejectedStratagem.class;

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

    public RequestAuthItem(List<String> path, String permission, Class<? extends AutoAuthHandler> handlerClass, Class<? extends AutoAuthHandlerChain> handlerChainClass, AutoAuthHandlerChain handlerChain, AutoAuthHandler handler, Class<? extends AuthRejectedStratagem> rejected) {
        this.path = path;
        this.permission = permission;
        this.handlerClass = handlerClass;
        this.handlerChainClass = handlerChainClass;
        this.handlerChain = handlerChain;
        this.handler = handler;
        this.rejected = rejected;
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

    public RequestAuthItem(List<String> path, String permission, AutoAuthHandlerChain handlerChain) {
        this.path = path;
        this.permission = permission;
        this.handlerChain = handlerChain;
    }

    public RequestAuthItem(List<String> path, String permission, AutoAuthHandler handler) {
        this.path = path;
        this.permission = permission;
        this.handler = handler;
    }

    public RequestAuthItem(List<String> path, String permission, Class<? extends AutoAuthHandler> handlerClass, Class<? extends AuthRejectedStratagem> rejectedClass) {
        this.path = path;
        this.permission = permission;
        this.handlerClass = handlerClass;
        this.rejected = rejectedClass;
    }
    public RequestAuthItem(List<String> path, String permission, AutoAuthHandlerChain handlerChain, Class<? extends AuthRejectedStratagem> rejectedClass) {
        this.path = path;
        this.permission = permission;
        this.handlerChain = handlerChain;
        this.rejected = rejectedClass;
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

    public AutoAuthHandlerChain getHandlerChain() {
        return handlerChain;
    }

    public void setHandlerChain(AutoAuthHandlerChain handlerChain) {
        this.handlerChain = handlerChain;
    }

    public AutoAuthHandler getHandler() {
        return handler;
    }

    public void setHandler(AutoAuthHandler handler) {
        this.handler = handler;
    }

    public Class<? extends AuthRejectedStratagem> getRejected() {
        return rejected;
    }

    public void setRejected(Class<? extends AuthRejectedStratagem> rejected) {
        this.rejected = rejected;
    }

    @Override
    public String toString() {
        return "RequestAuthItem{" + "path=" + path +
                ", permission='" + permission + '\'' +
                ", handlerClass=" + handlerClass +
                ", handlerChainClass=" + handlerChainClass +
                ", handlerChain=" + handlerChain +
                ", handler=" + handler +
                '}';
    }
}
