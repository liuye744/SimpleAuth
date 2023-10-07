package com.codingcube.simpleauth.autoconfig.domain;

import java.util.List;

public class HandlerChain {
    String id;
    List<Handler> handlerList;
    String pathId;
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
