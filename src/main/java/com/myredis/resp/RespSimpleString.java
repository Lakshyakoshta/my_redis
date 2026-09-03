package com.myredis.resp;

public class RespSimpleString implements RespValue {

    private final String value;

    public RespSimpleString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}