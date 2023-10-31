package com.codingcube.simpleauth.autoconfig.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.auth.strategic.DefaultItemStrategic;
import com.codingcube.simpleauth.auth.strategic.DefaultSignStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.limit.strategic.DefaultEffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.DefaultLimitRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;

public class Limit {
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "times")
    private Integer times;
    @JSONField(name = "seconds")
    private Integer seconds;
    @JSONField(name = "ban")
    private Integer ban;
    @JSONField(name = "itemStrategic")
    private String itemStrategic;
    private Class<? extends SignStrategic> itemStrategicClass;
    @JSONField(name = "signStrategic")
    private String signStrategic;
    private Class<? extends SignStrategic> signStrategicClass;
    @JSONField(name = "effectiveStrategic")
    private String effectiveStrategic;
    private Class<? extends EffectiveStrategic> effectiveStrategicClass;
    @JSONField(name = "tokenLimit")
    private String tokenLimit;
    private Class<? extends TokenLimit> tokenLimitClass;
    @JSONField(name = "rejected")
    private String rejected;
    private Class<? extends RejectedStratagem> rejectedClass;
    @JSONField(name = "pathsId")
    private String pathId;
    @JSONField(name = "paths")
    private Paths paths;

    public Limit(String id) {
        this.id = id;
    }

    public Limit(String id, Integer times, Integer seconds, Integer ban,
                 String itemStrategic, Class<? extends SignStrategic> itemStrategicClass,
                 String signStrategic, Class<? extends SignStrategic> signStrategicClass,
                 String effectiveStrategic, Class<? extends EffectiveStrategic> effectiveStrategicClass,
                 String tokenLimit, Class<? extends TokenLimit> tokenLimitClass) {
        this.id = id;
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.itemStrategic = itemStrategic;
        this.itemStrategicClass = itemStrategicClass;
        this.signStrategic = signStrategic;
        this.signStrategicClass = signStrategicClass;
        this.effectiveStrategic = effectiveStrategic;
        this.effectiveStrategicClass = effectiveStrategicClass;
        this.tokenLimit = tokenLimit;
        this.tokenLimitClass = tokenLimitClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times==null ? 100:times;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds==null ? 300:seconds;
    }

    public Integer getBan() {
        return ban;
    }

    public void setBan(Integer ban) {
        this.ban = ban==null ? 0:ban;
    }

    public String getItemStrategic() {
        return itemStrategic;
    }

    public void setItemStrategic(String itemStrategic) {
        if (itemStrategic == null){
            this.itemStrategicClass = DefaultItemStrategic.class;
        }
        this.itemStrategic = itemStrategic;
    }

    public Class<? extends SignStrategic> getItemStrategicClass() {
        return itemStrategicClass;
    }

    public void setItemStrategicClass(Class<? extends SignStrategic> itemStrategicClass) {
        this.itemStrategicClass = itemStrategicClass==null ? DefaultItemStrategic.class:itemStrategicClass;
    }

    public String getSignStrategic() {
        return signStrategic;
    }

    public void setSignStrategic(String signStrategic) {
        if (signStrategic == null) {
            this.signStrategicClass = DefaultSignStrategic.class;
        }else {
            this.signStrategic = signStrategic;
        }
    }

    public Class<? extends SignStrategic> getSignStrategicClass() {
        return signStrategicClass;
    }

    public void setSignStrategicClass(Class<? extends SignStrategic> signStrategicClass) {
        this.signStrategicClass = signStrategicClass==null ? DefaultSignStrategic.class:signStrategicClass;
    }

    public String getEffectiveStrategic() {
        return effectiveStrategic;
    }

    public void setEffectiveStrategic(String effectiveStrategic) {
        if (effectiveStrategic == null){
            this.effectiveStrategicClass = DefaultEffectiveStrategic.class;
        }
        this.effectiveStrategic = effectiveStrategic;
    }

    public Class<? extends EffectiveStrategic> getEffectiveStrategicClass() {
        return effectiveStrategicClass;
    }

    public void setEffectiveStrategicClass(Class<? extends EffectiveStrategic> effectiveStrategicClass) {
        this.effectiveStrategicClass = effectiveStrategicClass ==null ? DefaultEffectiveStrategic.class:effectiveStrategicClass;
    }

    public String getTokenLimit() {
        return tokenLimit;
    }

    public void setTokenLimit(String tokenLimit) {
        if (tokenLimit == null){
            this.tokenLimitClass = CompleteLimit.class;
        }
        this.tokenLimit = tokenLimit;
    }

    public Class<? extends TokenLimit> getTokenLimitClass() {
        return tokenLimitClass;
    }

    public void setTokenLimitClass(Class<? extends TokenLimit> tokenLimitClass) {
        this.tokenLimitClass = tokenLimitClass ==null ? CompleteLimit.class:tokenLimitClass;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public Class<? extends RejectedStratagem> getRejectedClass() {
        return rejectedClass;
    }

    public void setRejectedClass(Class<? extends RejectedStratagem> rejectedClass) {
        this.rejectedClass = rejectedClass ==null ? DefaultLimitRejectedStratagem.class:rejectedClass;
    }

    public Paths getPaths() {
        return paths;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public void setPaths(Paths paths) {
        this.paths = paths;
    }
}
