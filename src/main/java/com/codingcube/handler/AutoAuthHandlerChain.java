package com.codingcube.handler;

import com.codingcube.exception.TargetNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CodingCube<br>
 * Authentication Handler Chain*
 */
public abstract class AutoAuthHandlerChain {
    private final List<Object> autoAuthServiceList = new ArrayList<>();

    public AutoAuthHandlerChain() {
        addChain();
    }

    public abstract void addChain();

    final public void addLast(Class<? extends AutoAuthHandler> auto){
        autoAuthServiceList.add(auto);
    }

    final public void addLast(String beanName){
        autoAuthServiceList.add(beanName);
    }

    final public void addFirst(Class<? extends AutoAuthHandler> auto){
        autoAuthServiceList.add(0, auto);
    }

    final public void addFirst(String beanName){
        autoAuthServiceList.add(0, beanName);
    }

    final public void addAfter(Class<? extends AutoAuthHandler> auto, Object target){
        addAfterObject(auto, target);
    }

    final public void addAfter(String beanName, Object target){
        addAfterObject(beanName, target);
    }

    final public void addBefore(Class<? extends AutoAuthHandler> auto, Object target){
        addBeforeObject(auto, target);
    }

    final public void addBefore(String beanName, Object target){
        addBeforeObject(beanName, target);
    }

    private void addAfterObject(Object obj, Object target){
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
    }

    private void addBeforeObject(Object obj, Object target){
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
    }


    final public List<Object> getAutoAuthServiceList() {
        return autoAuthServiceList;
    }
}
