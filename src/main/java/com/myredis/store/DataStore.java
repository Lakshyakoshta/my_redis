package com.myredis.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private final Map<String, String> data =
            new ConcurrentHashMap<>();

    public void set(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean exists(String key) {
        return data.containsKey(key);
    }

    public boolean delete(String key) {
        return data.remove(key) != null;
    }
}