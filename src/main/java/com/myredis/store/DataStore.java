package com.myredis.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private final Map<String, Entry> data =
            new ConcurrentHashMap<>();

    public void set(String key, String value) {

        data.put(
                key,
                new Entry(value, 0)
        );
    }

    public String get(String key) {

        Entry entry =
                data.get(key);

        if (entry == null) {
            return null;
        }

        return entry.getValue();
    }

    public boolean exists(String key) {

        return data.containsKey(key);
    }

    public boolean delete(String key) {

        return data.remove(key) != null;
    }
}