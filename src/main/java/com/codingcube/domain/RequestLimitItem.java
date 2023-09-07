package com.codingcube.domain;

import com.codingcube.strategic.DefaultItemStrategic;
import com.codingcube.strategic.DefaultSignStrategic;
import com.codingcube.strategic.EffectiveStrategic;
import com.codingcube.strategic.SignStrategic;

import java.util.ArrayList;
import java.util.List;

public class RequestLimitItem {
    private List<String> path;
    private Integer times;
    private Integer seconds;
    private Integer ban;
    private Class<? extends SignStrategic> itemStrategic = DefaultItemStrategic.class;
    private Class<? extends SignStrategic> signStrategic = DefaultSignStrategic.class;

    public RequestLimitItem(List<String> path, Integer times, Integer seconds, Integer ban, Class<? extends SignStrategic> itemStrategic, Class<? extends SignStrategic> signStrategic) {
        this.path = path;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.itemStrategic = itemStrategic;
        this.signStrategic = signStrategic;
    }

    public RequestLimitItem(String path, Integer times, Integer seconds, Integer ban, Class<? extends SignStrategic> itemStrategic, Class<? extends SignStrategic> signStrategic) {
        List<String> paths = new ArrayList<>(1);
        paths.add(path);
        this.path = paths;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.itemStrategic = itemStrategic;
        this.signStrategic = signStrategic;
    }

    public RequestLimitItem(List<String> path, Integer times, Integer seconds, Integer ban) {
        this.path = path;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
    }

    public RequestLimitItem(String path, Integer times, Integer seconds, Integer ban) {
        List<String> paths = new ArrayList<>(1);
        paths.add(path);
        this.path = paths;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
    }


    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getBan() {
        return ban;
    }

    public void setBan(Integer ban) {
        this.ban = ban;
    }

    public Class<? extends SignStrategic> getItemStrategic() {
        return itemStrategic;
    }

    public void setItemStrategic(Class<? extends SignStrategic> itemStrategic) {
        this.itemStrategic = itemStrategic;
    }

    public Class<? extends SignStrategic> getSignStrategic() {
        return signStrategic;
    }

    public void setSignStrategic(Class<? extends SignStrategic> signStrategic) {
        this.signStrategic = signStrategic;
    }
}
