package com.codingcube.simpleauth.properties;

import com.codingcube.simpleauth.auth.handler.AutoAuthHandler;
import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.autoconfig.execption.ConfigurationParseException;
import com.codingcube.simpleauth.exception.TargetNotFoundException;
import com.codingcube.simpleauth.limit.cache.KeyCache;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenBucket;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import com.codingcube.simpleauth.validated.strategic.ValidateRejectedStratagem;
import org.springframework.stereotype.Component;

@Component
public class Bean2Static {

    public Bean2Static(LogProper logProper, FunctionProper functionProper, AuthProper authProper, LimitProper limitProper, ValidateProper validateProper) {
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

        AuthProper.setVerifyKeyStatic(authProper.getVerifyKey());
        AuthProper.setVerifyValueStatic(authProper.getVerifyValue());

        LogProper.setDateFormatStatic(logProper.getDateFormat());

        //AuthProper
        try {
            final Class<? extends AutoAuthHandler> defaultHandlerClazz = (Class<? extends AutoAuthHandler>) Class.forName(authProper.getDefaultHandler());
            AuthProper.setDefaultHandlerClazz(defaultHandlerClazz);

            final Class<? extends AuthRejectedStratagem> defaultRejectedClazz = (Class<? extends AuthRejectedStratagem>) Class.forName(authProper.getDefaultRejected());
            AuthProper.setDefaultRejectedClazz(defaultRejectedClazz);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationParseException(authProper.getDefaultRejected()+" not found");
        }

        //LimitProper
        try {
            final Class<? extends RejectedStratagem> defaultRejectedClazz = (Class<? extends RejectedStratagem>) Class.forName(limitProper.getDefaultRejected());
            LimitProper.setDefaultRejectedClazz(defaultRejectedClazz);

            final Class<? extends KeyCache> defaultBanCacheClazz = (Class<? extends KeyCache>) Class.forName(limitProper.getDefaultBanCache());
            LimitProper.setDefaultBanCacheClazz(defaultBanCacheClazz);

            final Class<? extends KeyCache> defaultLimitCacheClazz = (Class<? extends KeyCache>) Class.forName(limitProper.getDefaultLimitCache());
            LimitProper.setDefaultLimitCacheClazz(defaultLimitCacheClazz);

            final Class<? extends KeyCache> defaultSecondsCacheClazz = (Class<? extends KeyCache>) Class.forName(limitProper.getDefaultSecondsCache());
            LimitProper.setDefaultSecondsCacheClazz(defaultSecondsCacheClazz);

        } catch (ClassNotFoundException e) {
            throw new ConfigurationParseException(authProper.getDefaultRejected()+" not found");
        }

        //ValidateProper
        try {
            final Class<? extends ValidateRejectedStratagem> defaultRejectedClazz = (Class<? extends ValidateRejectedStratagem>) Class.forName(validateProper.getDefaultRejected());
            ValidateProper.setDefaultRejectedClazz(defaultRejectedClazz);

            final Class<?> defaultValidateObjectClazz =  Class.forName(validateProper.getDefaultValidateObject());
            ValidateProper.setDefaultValidateObjectClazz(defaultValidateObjectClazz);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationParseException(authProper.getDefaultRejected()+" not found");
        }
    }
}
