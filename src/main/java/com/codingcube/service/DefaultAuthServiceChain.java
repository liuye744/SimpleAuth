package com.codingcube.service;

import org.springframework.stereotype.Component;

@Component
public class DefaultAuthServiceChain extends AutoAuthServiceChain {
    @Override
    public void addChain() {
        //鉴权链置空
    }
}
