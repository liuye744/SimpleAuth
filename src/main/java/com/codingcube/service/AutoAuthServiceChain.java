package com.codingcube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AutoAuthServiceChain {
    @Resource
    private ApplicationContext applicationContext;
    List<Object> autoAuthServiceList = new ArrayList<>();

    public AutoAuthServiceChain() {
        addChain();
    }

    public abstract void addChain();

    final public void addAutoAuthService(Class<? extends AutoAuthService> auto){
        autoAuthServiceList.add(auto);
    }

    final public void addAutoAuthService(String beanName){
        autoAuthServiceList.add(beanName);
    }

    final public List<Object> getAutoAuthServiceList() {
        return autoAuthServiceList;
    }
}
