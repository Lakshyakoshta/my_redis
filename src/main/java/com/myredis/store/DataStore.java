package com.myredis.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private final Map<String, Entry> data =
            new ConcurrentHashMap<>();

    public void set(String key, String value) {

        set(key, value, 0);
    }

    public void set(
            String key,
            String value,
            long ttlMillis) {

        long expiresAt = 0;

        if (ttlMillis > 0) {

            expiresAt =
                    System.currentTimeMillis() + ttlMillis;
        }

        data.put(
                key,
                new Entry(value, expiresAt)
        );
    }

    public String get(String key) {

        Entry entry =
                getEntry(key);

        if (entry == null) {
            return null;
        }

        return entry.getValue();
    }

    public boolean exists(String key) {

        return getEntry(key) != null;
    }

    public boolean delete(String key) {

        return data.remove(key) != null;
    }

    public long ttl(String key) {

        Entry entry =
                getEntry(key);

        if (entry == null) {
            return -2;
        }

        long expiresAt =
                entry.getExpiresAt();

        if (expiresAt == 0) {
            return -1;
        }

        long remainingMillis =
                expiresAt - System.currentTimeMillis();

        if (remainingMillis <= 0) {

            data.remove(key);

            return -2;
        }

        return remainingMillis / 1000;
    }

    private Entry getEntry(String key) {

        Entry entry =
                data.get(key);

        if (entry == null) {
            return null;
        }

        long expiresAt =
                entry.getExpiresAt();

        if (expiresAt != 0 &&
                System.currentTimeMillis() >= expiresAt) {

            data.remove(key);

            return null;
        }

        return entry;
    }
}