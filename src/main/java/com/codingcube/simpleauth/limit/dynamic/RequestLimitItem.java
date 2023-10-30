package com.codingcube.simpleauth.limit.dynamic;

import com.codingcube.simpleauth.auth.strategic.DefaultItemStrategic;
import com.codingcube.simpleauth.auth.strategic.DefaultSignStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.limit.strategic.DefaultEffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;

import java.util.ArrayList;
import java.util.List;

public class RequestLimitItem {
    private List<String> path;
    private Integer times;
    private Integer seconds;
    private Integer ban;
    private Class<? extends SignStrategic> itemStrategic = DefaultItemStrategic.class;
    private Class<? extends SignStrategic> signStrategic = DefaultSignStrategic.class;
    private Class<? extends EffectiveStrategic> effectiveStrategic = DefaultEffectiveStrategic.class;
    private Class<? extends TokenLimit> tokenLimit = CompleteLimit.class;
    private Class<? extends RejectedStratagem> reject = DefaultLimitRejectedStratagem.class;

    public RequestLimitItem(List<String> path, Integer times, Integer seconds, Integer ban, Class<? extends SignStrategic> itemStrategic, Class<? extends SignStrategic> signStrategic, Class<? extends EffectiveStrategic> effectiveStrategic) {
        this.path = path;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.itemStrategic = itemStrategic;
        this.signStrategic = signStrategic;
        this.effectiveStrategic = effectiveStrategic;
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

    public RequestLimitItem(List<String> path, Integer times, Integer seconds, Integer ban, Class<? extends SignStrategic> itemStrategic, Class<? extends SignStrategic> signStrategic, Class<? extends EffectiveStrategic> effectiveStrategic, Class<? extends TokenLimit> tokenLimit) {
        this.path = path;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.itemStrategic = itemStrategic;
        this.signStrategic = signStrategic;
        this.effectiveStrategic = effectiveStrategic;
        this.tokenLimit = tokenLimit;
    }

    public RequestLimitItem(List<String> path, Integer times, Integer seconds, Integer ban, Class<? extends SignStrategic> itemStrategic, Class<? extends SignStrategic> signStrategic, Class<? extends EffectiveStrategic> effectiveStrategic, Class<? extends TokenLimit> tokenLimit, Class<? extends RejectedStratagem> reject) {
        this.path = path;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.itemStrategic = itemStrategic;
        this.signStrategic = signStrategic;
        this.effectiveStrategic = effectiveStrategic;
        this.tokenLimit = tokenLimit;
        this.reject = reject;
    }

    public Class<? extends TokenLimit> getTokenLimit() {
        return tokenLimit;
    }

    public void setTokenLimit(Class<? extends TokenLimit> tokenLimit) {
        this.tokenLimit = tokenLimit;
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

    public Class<? extends EffectiveStrategic> getEffectiveStrategic() {
        return effectiveStrategic;
    }

    public void setEffectiveStrategic(Class<? extends EffectiveStrategic> effectiveStrategic) {
        this.effectiveStrategic = effectiveStrategic;
    }

    public Class<? extends RejectedStratagem> getReject() {
        return reject;
    }

    public void setReject(Class<? extends RejectedStratagem> reject) {
        this.reject = reject;
    }
}
