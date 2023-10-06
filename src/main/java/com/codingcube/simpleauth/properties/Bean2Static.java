package com.codingcube.simpleauth.properties;

import com.codingcube.simpleauth.exception.TargetNotFoundException;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenBucket;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import org.springframework.stereotype.Component;

@Component
public class Bean2Static {

    public Bean2Static(LogProper logProper, FunctionProper functionProper) {
        LogProper.setStaticShowOptList(logProper.getShowOptList());

        FunctionProper.setHandlerCacheStatic(functionProper.isHandlerCache());

        //初始化全局限流策略
        final String limitPlan = functionProper.getLimitPlan().toLowerCase();
        switch (limitPlan){
            case  "default":
                FunctionProper.setTokenLimitClass(CompleteLimit.class);
                break;
            case "tokenbucket":
                FunctionProper.setTokenLimitClass(TokenBucket.class);
                break;
            default:
                try {
                    FunctionProper.setTokenLimitClass((Class<? extends TokenLimit>) Class.forName(limitPlan));
                } catch (ClassNotFoundException e) {
                    FunctionProper.setTokenLimitClass(CompleteLimit.class);
                    throw new TargetNotFoundException("Initialization error, CompleteLimit has been assembled.", e);
                }
        }
    }
}
