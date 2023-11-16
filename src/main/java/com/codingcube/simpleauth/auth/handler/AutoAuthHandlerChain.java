package com.codingcube.simpleauth.auth.handler;

import com.codingcube.simpleauth.exception.TargetNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CodingCube<br>
 * Authentication Handler Chain
 */
public abstract class AutoAuthHandlerChain {
     protected List<Object> autoAuthServiceList = new ArrayList<>();

    public AutoAuthHandlerChain() {
        addChain();
    }

    public abstract void addChain();

    final public AutoAuthHandlerChain add(String beanName){
        return addLast(beanName);
    }

    final public AutoAuthHandlerChain add(Class<? extends AutoAuthHandler> auto){
        autoAuthServiceList.add(auto);
        return this;
    }

    final public AutoAuthHandlerChain addLast(Class<? extends AutoAuthHandler> auto){
        autoAuthServiceList.add(auto);
        return this;
    }

    final public AutoAuthHandlerChain addLast(String beanName){
        autoAuthServiceList.add(beanName);
        return this;
    }

    final public AutoAuthHandlerChain addFirst(Class<? extends AutoAuthHandler> auto){
        autoAuthServiceList.add(0, auto);
        return this;
    }

    final public AutoAuthHandlerChain addFirst(String beanName){
        autoAuthServiceList.add(0, beanName);
        return this;
    }

    final public AutoAuthHandlerChain addAfter(Class<? extends AutoAuthHandler> auto, Object target){
        addAfterObject(auto, target);
        return this;
    }

    final public AutoAuthHandlerChain addAfter(String beanName, Object target){
        addAfterObject(beanName, target);
        return this;
    }

    final public AutoAuthHandlerChain addBefore(Class<? extends AutoAuthHandler> auto, Object target){
        addBeforeObject(auto, target);
        return this;
    }

    final public AutoAuthHandlerChain addBefore(String beanName, Object target){
        addBeforeObject(beanName, target);
        return this;
    }

    private AutoAuthHandlerChain addAfterObject(Object obj, Object target){
        boolean isFind = false;
        for (int i = 0; i < autoAuthServiceList.size(); i++) {
            final Object item = autoAuthServiceList.get(i);
            if (item.equals(target)){
                autoAuthServiceList.add(i+1, obj);
                i++;
                isFind = true;
            }
        }
        if (!isFind){
            throw new TargetNotFoundException("Target not found");
        }
        return this;
    }

    private AutoAuthHandlerChain addBeforeObject(Object obj, Object target){
        boolean isFind = false;
        for (int i = 0; i < autoAuthServiceList.size(); i++) {
            final Object item = autoAuthServiceList.get(i);
            if (item.equals(target)){
                autoAuthServiceList.add(i, obj);
                i++;
                isFind = true;
            }
        }
        if (!isFind){
            throw new TargetNotFoundException("Target not found");
        }
        return this;
    }


    final public List<Object> getAutoAuthServiceList() {
        return autoAuthServiceList;
    }
}
