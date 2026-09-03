package com.myredis.resp;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RespValueTest {

    @Test
    void shouldCreateBulkString() {

        RespBulkString value = new RespBulkString("hello");

        assertEquals("hello", value.getValue());
    }

    @Test
    void shouldCreateInteger() {

        RespInteger value = new RespInteger(42);

        assertEquals(42, value.getValue());
    }

    @Test
    void shouldCreateError() {

        RespError value = new RespError("ERR unknown command");

        assertEquals("ERR unknown command", value.getMessage());
    }

    @Test
    void shouldCreateArray() {

        RespArray value = new RespArray(
                List.of(
                        new RespBulkString("GET"),
                        new RespBulkString("name")
                )
        );

        assertEquals(2, value.getValues().size());
        assertEquals(
                "GET",
                ((RespBulkString) value.getValues().get(0)).getValue()
        );
        assertEquals(
                "name",
                ((RespBulkString) value.getValues().get(1)).getValue()
        );
    }
}