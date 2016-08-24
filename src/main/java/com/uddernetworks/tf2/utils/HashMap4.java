package com.uddernetworks.tf2.utils;

import java.util.HashMap;

public class HashMap4<K, V, T, Z> extends HashMap<K, V> {
    private HashMap<K, T> kt = new HashMap<>();
    private HashMap<K, Z> kz = new HashMap<>();

    public void setT(K key, T t) {
        kt.put(key, t);
    }
    public void setZ(K key, Z z) {
        kz.put(key, z);
    }

    public T getT(K key) {
        return kt.get(key);
    }

    public Z getZ(K key) {
        return kz.get(key);
    }
}