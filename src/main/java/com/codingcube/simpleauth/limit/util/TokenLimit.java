package com.codingcube.simpleauth.limit.util;

public interface TokenLimit {
    /**
     * 尝试获取*
     * @return 是否获取成功
     */
    boolean tryAcquire();
}
