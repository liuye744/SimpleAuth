package com.codingcube.simpleauth.autoconfig.domain;


public class Handler {
    final private String id;
    private String clazz;
    private String scope;
    private String pathId;
    private Paths paths;

    public Handler(String id, String clazz, String scope, String pathId) {
        this.id = id;
        this.clazz = clazz;
        this.scope = scope;
        this.pathId = pathId;
    }

    public Handler(String id, String pathId) {
        this.id = id;
        this.pathId = pathId;
    }

    public Handler(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public Paths getPaths() {
        return paths;
    }

    public void setPaths(Paths paths) {
        this.paths = paths;
    }

    public String getId() {
        return id;
    }
}
