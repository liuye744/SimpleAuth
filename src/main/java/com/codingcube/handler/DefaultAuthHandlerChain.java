package com.codingcube.handler;

import org.springframework.stereotype.Component;

@Component
public final class DefaultAuthHandlerChain extends AutoAuthHandlerChain {
    @Override
    public void addChain() {
        //鉴权链置空
    }
}
