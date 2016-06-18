package com.uddernetworks.tf2.utils;

import java.util.HashMap;

public class HashMap3<K, V, T> extends HashMap<K, V> {
    private HashMap<K, T> kt = new HashMap<K, T>();

    public void setT(K key, T t) {
        kt.put(key, t);
    }

    public T getT(K key) {
        return kt.get(key);
    }
}