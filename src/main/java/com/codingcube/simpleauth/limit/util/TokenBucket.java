package com.codingcube.simpleauth.limit.util;

import java.util.concurrent.Semaphore;

public class TokenBucket implements TokenLimit{
    private final int capacity; // 令牌桶容量
    private final double fillRate; // 令牌生成速率 (令牌/毫秒)
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

    private void refill() {
        long currentTime = System.currentTimeMillis();
        double elapsedTime = currentTime - lastRefillTimestamp;
        double newTokens = elapsedTime * fillRate;

        if (newTokens > 0) {
            tokens = Math.min(capacity, tokens + newTokens);
            lastRefillTimestamp = currentTime;
        }
    }

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
