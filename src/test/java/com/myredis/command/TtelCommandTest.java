package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TtlCommandTest {

    @Test
    void shouldReturnTtlForExpiringKey()
            throws InterruptedException {

        DataStore store = new DataStore();

        store.set(
                "name",
                "Lakshya",
                5000
        );

        TtlCommand command =
                new TtlCommand(store);

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
                integer.getValue() >= 4 &&
                integer.getValue() <= 5
        );
    }

    @Test
    void shouldReturnNegativeOneForNonExpiringKey() {

        DataStore store = new DataStore();

        store.set(
                "name",
                "Lakshya"
        );

        TtlCommand command =
                new TtlCommand(store);

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

        TtlCommand command =
                new TtlCommand(store);

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

        TtlCommand command =
                new TtlCommand(store);

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
                "ERR wrong number of arguments for 'ttl' command",
                error.getMessage()
        );
    }
}