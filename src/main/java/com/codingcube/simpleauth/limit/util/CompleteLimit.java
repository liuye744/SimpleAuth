package com.codingcube.simpleauth.limit.util;

import com.codingcube.simpleauth.limit.LimitInfoUtil;

import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CompleteLimit implements TokenLimit{
    Deque<Date> optList =  new LinkedList<>();
    /**
     * 信号量用于控制令牌的发放
     */
    private final Semaphore semaphore;
    /**
     * 限制次数
     */
    private Integer limit;
    /**
     * 限制时间seconds
     */
    private Integer seconds;
    /**
     *  SECOND_SCALE
     */
    private final static Long SECOND_SCALE = 1000L;

    public CompleteLimit() {
        this.semaphore = new Semaphore(1);
    }

    public CompleteLimit(Integer limit, Integer seconds) {
        this.semaphore = new Semaphore(1);
        this.seconds = seconds;
        this.limit = limit;
    }

    @Override
    public void init(Integer limit, Integer seconds) {
        this.seconds = seconds;
        this.limit = limit;
    }

    @Override
    public void init(int capacity, double fillRate) {
        this.seconds = Math.toIntExact(Math.round(capacity / fillRate));
        this.limit = capacity;
    }

    @Override
    public void removeFirst() {
        synchronized (semaphore){
            optList.removeFirst();
        }
    }

    @Override
    public int size() {
        return optList.size();
    }

    @Override
    public int optSize() {
        return this.limit - size();
    }

    @Override
    public int maxOptSize() {
        return this.limit;
    }

    @Override
    public void sync() {
        Date currentDate = new Date();
        while (optList.size()>0 &&(currentDate.getTime() - optList.getLast().getTime())/1000 > seconds){
            optList.removeLast();
        }
    }

    @Override
    public Object getSyncMutex() {
        return semaphore;
    }

    @Override
    public String toString() {
        return optList.toString();
    }

    @Override
    public boolean tryAcquire() {
        synchronized (semaphore){
            if (optList.size()>=limit){
                //Remove expired data before judging, and return false if it still overflows.
                while (optList.size() > 0){
                    final Date last = optList.getLast();
                    final Date current = new Date();
                    if ((current.getTime() - last.getTime()) > seconds*SECOND_SCALE){
                        //expired
                        optList.removeLast();
                    }else {
                        break;
                    }
                }
                //After deleting expired records, the number of times the limit is met.
                if (optList.size()<limit){
                    optList.addFirst(new Date());
                    return true;
                }
                return false;
            }else {
                //Judge whether the top level has expired and add records.
                if (optList.size()==0){
                    optList.add(new Date());
                    return true;
                }
                final Date last = optList.getLast();
                final Date current = new Date();
                if ((current.getTime() - last.getTime()) > seconds*SECOND_SCALE){
                    //expired
                    optList.removeLast();
                }else {
                    optList.addFirst(new Date());

                }
                return true;
            }

        }
    }

}
