package com.codingcube.simpleauth.autoconfig.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class HandlerChain {
    @JSONField(name = "tokenLimit")
    String id;
    @JSONField(name = "list")
    List<Handler> handlerList;
    @JSONField(name = "pathsId")
    String pathId;
    @JSONField(name = "paths")
    Paths paths;

    public HandlerChain(String id) {
        this.id = id;
    }

    public HandlerChain(String id, List<Handler> handlerList) {
        this.id = id;
        this.handlerList = handlerList;
    }

    public HandlerChain(String id, List<Handler> handlerList, Paths paths) {
        this.id = id;
        this.handlerList = handlerList;
        this.paths = paths;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Handler> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(List<Handler> handlerList) {
        this.handlerList = handlerList;
    }

    public Paths getPaths() {
        return paths;
    }

    public void setPaths(Paths paths) {
        this.paths = paths;
    }


    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }
}
