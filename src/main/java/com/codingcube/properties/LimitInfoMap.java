package com.codingcube.properties;

import java.util.*;

public class LimitInfoMap {
    private static final Map<String,Map<String, Deque<Date>>> limitInfo = new HashMap<>();

    /**
     * append record*
     * @param recordItem record item
     * @param sign  record sign
     * @return Whether it overflows
     */
    public static Boolean addRecord(String recordItem, String sign, Integer limit, Integer seconds){
        if (limitInfo.get(recordItem) == null){
            synchronized (recordItem.intern()){
                if (limitInfo.get(recordItem) == null){
                    //No record exists.
                    Map<String,Deque<Date>> item =  new HashMap<>();

                    Deque<Date> deque = new LinkedList<>();
                    deque.add(new Date());

                    item.put(sign, deque);
                    limitInfo.put(recordItem,item);
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
            return false;
        }else {
            //Judge whether the top level has expired and add records.
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
