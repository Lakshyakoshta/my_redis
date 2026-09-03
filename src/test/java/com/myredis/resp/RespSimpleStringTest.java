package com.myredis.resp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RespSimpleStringTest {

    @Test
    void shouldStoreSimpleStringValue() {

        RespSimpleString value = new RespSimpleString("OK");

        assertEquals("OK", value.getValue());
    }
}