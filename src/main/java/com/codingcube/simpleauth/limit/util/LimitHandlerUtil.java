package com.codingcube.simpleauth.limit.util;

import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.dynamic.RequestLimitItem;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.logging.Log;
import com.codingcube.simpleauth.logging.logformat.LogLimitFormat;
import com.codingcube.simpleauth.util.AuthHandlerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class LimitHandlerUtil {
    private final static String ACCORD_SIGN = "SIMPLEAUTH_VERIFY_SIGN";
    public static void preHandlerRequestLimitItem(List<RequestLimitItem> requestLimitItem,
                                                  AntPathMatcher antPathMatcher,
                                                  HttpServletRequest request,
                                                  ApplicationContext applicationContext,
                                                  Log log,
                                                  String source){
        final String requestURI = request.getRequestURI();
        for (RequestLimitItem limitItem : requestLimitItem) {
            final List<String> pathList = limitItem.getPath();
            for (String path : pathList) {
                if (antPathMatcher.match(path, requestURI)) {
                    //Create sign
                    SignStrategic signStrategic = AuthHandlerUtil.getBean(applicationContext, limitItem.getSignStrategic());
                    final String sign = signStrategic.sign(request, null);

                    //Verify that this request is recorded.
                    final SignStrategic itemStrategic = AuthHandlerUtil.getBean(applicationContext, limitItem.getItemStrategic());
                    final String item = itemStrategic.sign(request, null);

                    request.setAttribute(ACCORD_SIGN + source, new AttributeItem(limitItem, sign, item));

                    final Boolean addRecord = LimitInfoUtil.addRecord(item, sign, limitItem.getTimes(),
                            limitItem.getSeconds(), limitItem.getBan(), limitItem.getTokenLimit());
                    if (!addRecord) {
                        LogLimitFormat limitFormat = new LogLimitFormat(limitItem.getTimes(), limitItem.getSeconds(), limitItem.getBan(), item, limitItem.getSignStrategic(), sign,
                                source, true, limitItem.getEffectiveStrategic(), true, false);
                        log.debug(limitFormat.toString());
                        final Class<? extends RejectedStratagem> reject = limitItem.getReject();
                        final RejectedStratagem bean = AuthHandlerUtil.getBean(applicationContext, reject);
                        bean.doRejected(request,
                                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse(),
                                limitFormat);
                        return;
                    }
                }
            }
        }
    }

    public static void postHandlerRequestLimitItem(HttpServletRequest request,
                                                   ApplicationContext applicationContext,
                                                   Log log,
                                                   Object result,
                                                   String source){
        final AttributeItem attributeItem = (AttributeItem) request.getAttribute(ACCORD_SIGN + source);
        RequestLimitItem limitItem  = attributeItem.getLimitItem();
        if (limitItem == null){
            return;
        }
        final String sign = attributeItem.getSign();
        final String item = attributeItem.getItem();

        EffectiveStrategic effectiveStrategicInstance = AuthHandlerUtil.getBean(applicationContext, limitItem.getEffectiveStrategic());
        final Boolean isEffective = effectiveStrategicInstance.effective(request,null, result);
        LogLimitFormat limitFormat = new LogLimitFormat(limitItem.getTimes(), limitItem.getSeconds(), limitItem.getBan(), item, limitItem.getSignStrategic(), sign,
                source, true, limitItem.getEffectiveStrategic(), isEffective, true);
        log.debug(limitFormat.toString());
        if (!isEffective){
            LimitInfoUtil.delRecord(item, sign);
        }
    }
    private static class AttributeItem{
        final private RequestLimitItem limitItem;
        final private String sign;
        final private String item;

        public AttributeItem(RequestLimitItem limitItem, String sign, String item) {
            this.limitItem = limitItem;
            this.sign = sign;
            this.item = item;
        }

        public RequestLimitItem getLimitItem() {
            return limitItem;
        }

        public String getSign() {
            return sign;
        }

        public String getItem() {
            return item;
        }
    }
}
