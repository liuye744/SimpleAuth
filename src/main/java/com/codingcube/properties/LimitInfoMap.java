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
    public static Boolean addRecord(String recordItem, String sign, Integer limit, Integer seconds, Integer max){
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
        synchronized (sign.intern()){
            if (personalRecord.size() == 0){
                personalRecord.addFirst(new Date());
                return true;
            }
            Date last = personalRecord.getLast();
            final Date current = new Date();
            if (personalRecord.size()>=limit){
                if (personalRecord.size() == limit){
                    if ((current.getTime() - last.getTime())/1000 > seconds){
                        personalRecord.removeLast();
                        personalRecord.addFirst(current);
                        return true;
                    }else {
                        personalRecord.addLast(personalRecord.getLast());
                        return false;
                    }
                }else {
                    if ((current.getTime() - last.getTime())/1000 > max){
                        while ((current.getTime() - last.getTime())/1000 > seconds){
                            personalRecord.removeLast();
                            if (personalRecord.size() != 0){
                                last = personalRecord.getLast();
                            }else {
                                break;
                            }
                        }
                        personalRecord.addFirst(new Date());
                        return true;
                    }else {
                        return false;
                    }
                }

            }else {
                //Judge whether the top level has expired and add records.
                if ((current.getTime() - last.getTime())/1000 > seconds){
                    //expired
                    personalRecord.removeLast();
                }
                personalRecord.addFirst(new Date());
                return true;
            }

        }
    }
}
