package com.myredis.store;

public class Entry {

    private final String value;

    private final long expiresAt;

    public Entry(
            String value,
            long expiresAt) {

        this.value = value;
        this.expiresAt = expiresAt;
    }

    public String getValue() {
        return value;
    }

    public long getExpiresAt() {
        return expiresAt;
    }
}