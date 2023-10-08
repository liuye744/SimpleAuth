package com.codingcube.simpleauth.auth.handler;

import com.codingcube.simpleauth.autoconfig.domain.HandlerChain;

import java.util.List;

public class DynamicHandlerChain extends AutoAuthHandlerChain {
    public DynamicHandlerChain(List<Object> autoAuthServiceList) {
        this.autoAuthServiceList = autoAuthServiceList;
    }

    @Override
    public void addChain() {
        //pass
    }
}
