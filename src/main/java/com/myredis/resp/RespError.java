package com.myredis.resp;

public class RespError implements RespValue {

    private final String message;

    public RespError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}