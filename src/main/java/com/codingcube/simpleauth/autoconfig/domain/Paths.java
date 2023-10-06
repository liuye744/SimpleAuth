package com.codingcube.simpleauth.autoconfig.domain;

import java.util.List;

public class Paths {
    final private String id;
    private String permission;
    private List<String> path;

    public Paths(String id, List<String> path) {
        this.id = id;
        this.path = path;
    }

    public Paths(String id, String permission, List<String> path) {
        this.id = id;
        this.permission = permission;
        this.path = path;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Paths(String id) {
        this.id = id;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Paths{" + "id='" + id + '\'' +
                ", path=" + path +
                '}';
    }
}
