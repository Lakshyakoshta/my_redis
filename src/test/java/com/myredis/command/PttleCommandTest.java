package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PttlCommandTest {

    @Test
    void shouldReturnPttlForExpiringKey() {

        DataStore store = new DataStore();

        store.set(
                "name",
                "Lakshya",
                5000
        );

        PttlCommand command =
                new PttlCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("name")
                        }
                );

        RespInteger integer =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertTrue(
                integer.getValue() > 0 &&
                integer.getValue() <= 5000
        );
    }

    @Test
    void shouldReturnNegativeOneForNonExpiringKey() {

        DataStore store = new DataStore();

        store.set(
                "name",
                "Lakshya"
        );

        PttlCommand command =
                new PttlCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("name")
                        }
                );

        RespInteger integer =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertEquals(
                -1,
                integer.getValue()
        );
    }

    @Test
    void shouldReturnNegativeTwoForMissingKey() {

        DataStore store = new DataStore();

        PttlCommand command =
                new PttlCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("missing")
                        }
                );

        RespInteger integer =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertEquals(
                -2,
                integer.getValue()
        );
    }

    @Test
    void shouldRejectWrongNumberOfArguments() {

        DataStore store = new DataStore();

        PttlCommand command =
                new PttlCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{}
                );

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR wrong number of arguments for 'pttl' command",
                error.getMessage()
        );
    }
}