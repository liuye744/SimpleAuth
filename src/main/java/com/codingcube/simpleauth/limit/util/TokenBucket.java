package com.codingcube.simpleauth.limit.util;

import java.util.concurrent.Semaphore;

public class TokenBucket implements TokenLimit{
    private int capacity; // 令牌桶容量
    private double fillRate; // 令牌生成速率 (令牌/秒)
    private long lastRefillTimestamp; // 上次令牌生成时间戳
    private double tokens; // 当前令牌数量
    private final Semaphore semaphore; // 信号量用于控制令牌的发放

    public TokenBucket(int capacity, double fillRate) {
        this.capacity = capacity;
        this.fillRate = fillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
        this.semaphore = new Semaphore(1);
    }

    public TokenBucket(int seconds, int limit) {
        this.capacity = limit;
        this.fillRate = (double) seconds/limit;
        this.tokens = limit;
        this.semaphore = new Semaphore(1);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public TokenBucket() {
        this.semaphore = new Semaphore(1);
    }

    public TokenBucket(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void init(Integer limit, Integer seconds) {
        this.capacity = limit;
        this.fillRate = (double) limit/seconds;
        this.tokens = limit;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public void init(int capacity, double fillRate) {
        this.capacity = capacity;
        this.fillRate = fillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public void removeFirst() {
        try {
            semaphore.acquire();
            tokens++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }
    }

    @Override
    public int size() {
        return (int) Math.round(capacity - tokens);
    }

    private void refill() {
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (double) (currentTime - lastRefillTimestamp)/1000;
        double newTokens = elapsedTime * fillRate;

        if (newTokens > 0) {
            tokens = Math.min(capacity, tokens + newTokens);
            lastRefillTimestamp = currentTime;
        }
    }

    @Override
    public String toString() {
        return "TokenBucket{" + "tokens=" + tokens +
                '}';
    }

    @Override
    public boolean tryAcquire() {
        refill();
        try {
            semaphore.acquire(); // 获取许可证，互斥访问
            if (tokens >= 1) {
                tokens--;
                return true; // 有足够的令牌，允许处理请求
            } else {
                return false; // 令牌不足，拒绝请求
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            semaphore.release(); // 释放许可证
        }
    }
}
