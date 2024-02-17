package com.codingcube.simpleauth.limit.cache;


import java.util.Set;

public interface KeyCache<T>{
    T remove(String key);

    T get(String key);

    T put(String key, T data);

    int size();

    boolean containsKey(String key);

    Set<String> keySet();

    void clear();
}
