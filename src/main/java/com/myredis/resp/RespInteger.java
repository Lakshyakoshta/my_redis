package com.myredis.resp;

public class RespInteger implements RespValue {

    private final long value;

    public RespInteger(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}