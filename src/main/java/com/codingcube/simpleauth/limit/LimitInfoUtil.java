package com.codingcube.simpleauth.limit;

import com.codingcube.simpleauth.limit.cache.KeyCache;
import com.codingcube.simpleauth.limit.cache.impl.DefaultCache;
import com.codingcube.simpleauth.limit.strategic.BanKeyStratagem;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import com.codingcube.simpleauth.properties.LimitProper;
import com.codingcube.simpleauth.util.AuthHandlerUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CodingCube<br>*
 * Core Utility Class for Access Limitation Feature*
 */
public class LimitInfoUtil {
    private static KeyCache<Map<String, TokenLimit>> limitInfo;
    private static KeyCache<Date> ban;
    private static KeyCache<Integer> secondsRecord;

    static {
        try {
            limitInfo = LimitProper.getDefaultLimitCacheClazz().getConstructor().newInstance();
            ban = LimitProper.getDefaultBanCacheClazz().getConstructor().newInstance();
            secondsRecord =LimitProper.getDefaultSecondsCacheClazz().getConstructor().newInstance();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * append record*
     * @param recordItem record item
     * @param sign  record sign
     * @return Whether it overflows
     */
    public static Boolean addRecord(String recordItem,
                                    String sign,
                                    Integer limit,
                                    Integer seconds,
                                    Integer banTime,
                                    Class<? extends TokenLimit> tokenLimitClass){
        final String banKey = BanKeyStratagem.getBanKey(recordItem, sign);
        //移除ban
        if (LimitInfoUtil.ban.get(banKey) != null && banTime!=0){
            synchronized (sign.intern()){
                final Date banData = LimitInfoUtil.ban.get(banKey);
                if (LimitInfoUtil.ban.get(banKey) != null){
                    if ((new Date().getTime()-banData.getTime())/1000 < banTime){
                        return false;
                    }else {
                        ban.remove(banKey);
                        limitInfo.get(recordItem).remove(sign);
                    }

                }
            }
        }
        if (limitInfo.get(recordItem) == null){
            synchronized (recordItem.intern()){
                if (limitInfo.get(recordItem) == null){
                    //No record exists.
                    Map<String, TokenLimit> item =  new ConcurrentHashMap<>();

                    final TokenLimit tokenLimit = AuthHandlerUtil.getParameterlessObject(tokenLimitClass);
                    tokenLimit.init(limit, seconds);


                    item.put(sign, tokenLimit);
                    limitInfo.put(recordItem,item);
                    secondsRecord.put(recordItem, seconds);
                    return tokenLimit.tryAcquire();
                }
            }
        }
        final Map<String, TokenLimit> stringListMap = limitInfo.get(recordItem);

        if (stringListMap.get(sign) == null){
            synchronized (sign.intern()){
                if (stringListMap.get(sign) == null){
                    //Personal record does not exist.
                    final TokenLimit tokenLimit = AuthHandlerUtil.getParameterlessObject(tokenLimitClass);
                    tokenLimit.init(limit, seconds);

                    stringListMap.put(sign, tokenLimit);
                    return tokenLimit.tryAcquire();
                }
            }
        }
        //All records exist.
        final TokenLimit tokenLimit = stringListMap.get(sign);
        final boolean valid = tokenLimit.tryAcquire();
        if (!valid){
            //被禁止
            LimitInfoUtil.ban.put(banKey, new Date());
        }
        return valid;
    }

    public static Boolean delRecord(String recordItem, String sign){
        final Map<String, TokenLimit> stringDequeMap = limitInfo.get(recordItem);
        if (stringDequeMap == null){
            return false;
        }
        final TokenLimit deque = stringDequeMap.get(sign);
        if (deque == null){
            return false;
        }
        if (deque.size()>0){
            synchronized (sign.intern()){
                deque.removeFirst();
            }
            return true;
        }
        return false;
    }

    /**
     * Get banned IP Map*
     * @return Map<IP, Ban Time>
     */
    public static KeyCache<Date> getBanMap(){
        return ban;
    }

    /**
     * *
     * @return Map<recordItem, Map<sign, Deque<Option Time>>>
     */
    public static KeyCache<Map<String, TokenLimit>> getLimitInfo(){
        return limitInfo;
    }

    /**
     * Get All RecordItem*
     * @return Set<RecordItem></>
     */
    public static Set<String> getAllRecordItem(){
        return limitInfo.keySet();
    }

    /**
     * Get sign's option List*
     * @return Deque<Option Date></>
     */
    public static TokenLimit getSignOptionList(String recordItem, String sign){
        final Map<String, TokenLimit> itemDeque = limitInfo.get(recordItem);
        if (itemDeque != null){
            return itemDeque.get(sign);
        }
        return null;
    }

    public static void setBan(String recordItem, String sign, Date date){
        final String banKey = BanKeyStratagem.getBanKey(recordItem, sign);
        ban.put(banKey, date);
    }
    public static void setBan(String recordItem, String sign){
        final String banKey = BanKeyStratagem.getBanKey(recordItem, sign);
        ban.put(banKey, new Date());
    }

    public static Map<String, TokenLimit> getRecordItem(String recordItem){
        return limitInfo.get(recordItem);
    }

    /**
     * Remove expired records*
     */
    public static void clearExpiredRecord(){
        final Set<String> recordSet = secondsRecord.keySet();
        recordSet.forEach(
                LimitInfoUtil::removeRecordItemOutDateRecord
        );
    }
    private static void removeRecordItemOutDateRecord(String sign){
        final Map<String, TokenLimit> operationQueue = limitInfo.get(sign);
        operationQueue.keySet().forEach(
                key->{
                    final TokenLimit tokenLimit = operationQueue.get(key);
                    tokenLimit.sync();
                    final Object syncMutex = tokenLimit.getSyncMutex();
                    synchronized (syncMutex){
                        if (tokenLimit.maxOptSize() == tokenLimit.optSize()){
                            operationQueue.remove(key);
                        }
                    }

                }
        );
    }
}
