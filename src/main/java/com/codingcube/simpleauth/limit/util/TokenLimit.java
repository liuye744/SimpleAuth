package com.codingcube.simpleauth.limit.util;

import java.util.concurrent.Semaphore;

public interface TokenLimit {
    /**
     * 尝试获取*
     * @return 是否获取成功
     */
    boolean tryAcquire();

    void init(Integer limit, Integer seconds);

    void init(int capacity, double fillRate);

    void removeFirst();

    int size();
}
