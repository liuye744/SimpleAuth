package com.codingcube.properties;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LimitInfoUtil {
    private static final Map<String, Map<String, Deque<Date>>> limitInfo = new ConcurrentHashMap<>();
    private static final Map<String, Date> ban = new ConcurrentHashMap<>();
    private static final Map<String, Integer> secondsRecord = new ConcurrentHashMap<>();

    /**
     * append record*
     * @param recordItem record item
     * @param sign  record sign
     * @return Whether it overflows
     */
    public static Boolean addRecord(String recordItem, String sign, Integer limit, Integer seconds, Integer banTime){
        final String banKey = BanKeyStratagem.getBanKey(recordItem, sign);
        if (LimitInfoUtil.ban.get(banKey) != null && banTime!=0){
            synchronized (sign.intern()){
                final Date banData = LimitInfoUtil.ban.get(banKey);
                if (LimitInfoUtil.ban.get(banKey) != null){
                    if ((new Date().getTime()-banData.getTime())/1000 < banTime){
                        return false;
                    }else {
                        ban.remove(banKey);
                        final Deque<Date> optionList = limitInfo.get(recordItem).get(sign);
                        while (optionList.size()!=0){
                            optionList.removeLast();
                        }
                    }

                }

            }
        }
        if (limitInfo.get(recordItem) == null){
            synchronized (recordItem.intern()){
                if (limitInfo.get(recordItem) == null){
                    //No record exists.
                    Map<String,Deque<Date>> item =  new ConcurrentHashMap<>();

                    Deque<Date> deque = new LinkedList<>();
                    deque.add(new Date());

                    item.put(sign, deque);
                    limitInfo.put(recordItem,item);
                    secondsRecord.put(recordItem, seconds);
                    return true;
                }
            }
        }
        final Map<String, Deque<Date>> stringListMap = limitInfo.get(recordItem);

        if (stringListMap.get(sign) == null){
            synchronized (sign.intern()){
                if (stringListMap.get(sign) == null){
                    //Personal record does not exist.
                    Deque<Date> deque = new LinkedList<>();
                    deque.addFirst(new Date());
                    stringListMap.put(sign, deque);
                    return true;
                }
            }
        }
        //All records exist.
        Deque<Date> personalRecord = stringListMap.get(sign);
        synchronized (sign.intern()){
            if (personalRecord.size()>=limit){
                //Remove expired data before judging, and return false if it still overflows.
                while (personalRecord.size() > 0){
                    final Date last = personalRecord.getLast();
                    final Date current = new Date();
                    if ((current.getTime() - last.getTime())/1000 > seconds){
                        //expired
                        personalRecord.removeLast();
                    }else {
                        break;
                    }
                }
                //After deleting expired records, the number of times the limit is met.
                if (personalRecord.size()<limit){
                    personalRecord.addFirst(new Date());
                    return true;
                }
                LimitInfoUtil.ban.put(banKey, new Date());
                return false;
            }else {
                //Judge whether the top level has expired and add records.
                if (personalRecord.size()==0){
                    personalRecord.add(new Date());
                    return true;
                }
                final Date last = personalRecord.getLast();
                final Date current = new Date();
                if ((current.getTime() - last.getTime())/1000 > seconds){
                    //expired
                    personalRecord.removeLast();
                }else {
                    personalRecord.addFirst(new Date());

                }
                return true;
            }
        }
    }

    /**
     * Get banned IP Map*
     * @return Map<IP, Ban Time>
     */
    public static Map<String, Date> getBanMap(){
        return ban;
    }

    /**
     * *
     * @return Map<recordItem, Map<sign, Deque<Option Time>>>
     */
    public static Map<String,Map<String, Deque<Date>>> getLimitInfo(){
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
    public static Deque<Date> getSignOptionList(String recordItem, String sign){
        return limitInfo.get(recordItem).get(sign);
    }

    public static void setBan(String recordItem, String sign, Date date){
        final String banKey = BanKeyStratagem.getBanKey(recordItem, sign);
        ban.put(banKey, date);
    }
    public static void setBan(String recordItem, String sign){
        final String banKey = BanKeyStratagem.getBanKey(recordItem, sign);
        ban.put(banKey, new Date());
    }

    public static Map<String, Deque<Date>> getRecordItem(String recordItem){
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
        final Map<String, Deque<Date>> operationQueue = limitInfo.get(sign);
        final Integer recordSeconds = secondsRecord.get(sign);
        operationQueue.keySet().forEach(
                key->{
                    final Deque<Date> deque = operationQueue.get(key);
                    Date currentDate = new Date();
                    while (deque.size()>0 &&(currentDate.getTime() - deque.getLast().getTime())/1000 > recordSeconds){
                        deque.removeLast();
                    }
                    //remove userRecord
                    if (deque.size()==0){
                        synchronized (key.intern()){
                            operationQueue.remove(key);
                        }
                    }
                }
        );
    }
}
