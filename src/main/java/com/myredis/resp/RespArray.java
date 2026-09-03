package com.myredis.resp;

import java.util.List;

public class RespArray implements RespValue {

    private final List<RespValue> values;

    public RespArray(List<RespValue> values) {
        this.values = values;
    }

    public List<RespValue> getValues() {
        return values;
    }
}