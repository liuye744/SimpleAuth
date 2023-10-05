package com.codingcube.simpleauth.autoconfig.domain;

public class Limit {
    String id;
    String clazz;
    String scope;
    String pathId;
    Paths paths;

    public Limit(String id) {
        this.id = id;
    }

    public Limit(String id, String clazz, String scope, String pathId) {
        this.id = id;
        this.clazz = clazz;
        this.scope = scope;
        this.pathId = pathId;
    }

    public Limit(String id, String clazz, String scope, Paths paths) {
        this.id = id;
        this.clazz = clazz;
        this.scope = scope;
        this.paths = paths;
    }

    public String getId() {
        return id;
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
}
