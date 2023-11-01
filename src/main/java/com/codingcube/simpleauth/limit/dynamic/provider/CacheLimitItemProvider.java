package com.codingcube.simpleauth.limit.dynamic.provider;

import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItemProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class CacheLimitItemProvider implements RequestLimitItemProvider {
    List<RequestLimitItem> limitItemList = new ArrayList<>();
    volatile boolean updateTag = true;
    final Object singletonMutex = new Object();

    @Override
    public final List<RequestLimitItem> getRequestLimitItem() {
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

    public abstract List<RequestLimitItem> getLimitItem();
}
