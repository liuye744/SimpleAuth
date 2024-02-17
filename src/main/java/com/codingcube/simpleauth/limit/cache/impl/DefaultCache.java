package com.codingcube.simpleauth.limit.cache.impl;

import com.codingcube.simpleauth.limit.cache.KeyCache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCache<T> implements KeyCache<T> {
    private final Map<String, T> ban = new ConcurrentHashMap<>();


    @Override
    public T remove(String key) {
        return ban.remove(key);
    }

    @Override
    public T get(String key) {
        return ban.get(key);
    }

    @Override
    public T put(String key, T data) {
        return ban.put(key, data);
    }

    @Override
    public int size() {
        return ban.size();
    }

    @Override
    public boolean containsKey(String key) {
        return ban.containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return ban.keySet();
    }

    @Override
    public void clear() {
        ban.clear();
    }
}
