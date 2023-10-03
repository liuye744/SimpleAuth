package com.codingcube.simpleauth.limit.util;

public interface TokenLimit {
    /**
     * Try to get*
     * @return Is it successful?
     */
    boolean tryAcquire();

    /**
     * init *
     * @param limit Limit the number of times
     * @param seconds Restricted time
     */
    void init(Integer limit, Integer seconds);

    /**
     * init *
     * @param capacity capacity
     * @param fillRate Token issuing speed
     */
    void init(int capacity, double fillRate);

    /**
     * Remove the "latest" operation. *
     */
    void removeFirst();

    /**
     * Requested quantity *
     */
    int size();

    /**
     * requestable amount *
     */
    int optSize();

    /**
     * max requestable amount *
     */
    int maxOptSize();

    /**
     * sync data *
     */
    void sync();

    Object getSyncMutex();
}
