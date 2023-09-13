package com.codingcube.simpleauth.properties;

import org.springframework.stereotype.Component;

@Component
public class Bean2Static {

    public Bean2Static(LogProper logProper, FunctionProper functionProper) {
        LogProper.setStaticShowOptList(logProper.getShowOptList());

        FunctionProper.setHandlerCacheStatic(functionProper.isHandlerCache());
    }
}
