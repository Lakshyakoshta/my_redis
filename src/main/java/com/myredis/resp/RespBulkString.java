package com.myredis.resp;

public class RespBulkString implements RespValue {

    private final String value;

    public RespBulkString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}