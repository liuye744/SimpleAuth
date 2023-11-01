package com.codingcube.simpleauth.auth.dynamic.provider;

import com.codingcube.simpleauth.auth.dynamic.RequestAuthItem;
import com.codingcube.simpleauth.auth.dynamic.RequestAuthItemProvider;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItemProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class CacheAuthItemProvider implements RequestAuthItemProvider {
    List<RequestAuthItem> limitItemList = new ArrayList<>();
    volatile boolean updateTag = true;
    final Object singletonMutex = new Object();

    @Override
    public final List<RequestAuthItem> getRequestAuthItem() {
        if (updateTag) {
            synchronized (singletonMutex) {
                if (updateTag){
                    limitItemList = getLimitItem();
                    updateTag = false;
                }
            }
        }
        return limitItemList;
    }

    public void update() {
        updateTag = true;
    }

    public abstract List<RequestAuthItem> getLimitItem();
}
