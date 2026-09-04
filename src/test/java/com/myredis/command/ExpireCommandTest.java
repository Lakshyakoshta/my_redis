package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpireCommandTest {

    @Test
    void shouldSetExpirationOnExistingKey() {

        DataStore store = new DataStore();

        store.set(
                "name",
                "Lakshya"
        );

        ExpireCommand command =
                new ExpireCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("name"),
                                new RespBulkString("10")
                        }
                );

        RespInteger integer =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertEquals(
                1,
                integer.getValue()
        );

        long pttl =
                store.pttl("name");

        assertTrue(
                pttl > 0 &&
                pttl <= 10000
        );
    }

    @Test
    void shouldReturnZeroForMissingKey() {

        DataStore store = new DataStore();

        ExpireCommand command =
                new ExpireCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("missing"),
                                new RespBulkString("10")
                        }
                );

        RespInteger integer =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertEquals(
                0,
                integer.getValue()
        );
    }

    @Test
    void shouldRejectInvalidExpiration() {

        DataStore store = new DataStore();

        ExpireCommand command =
                new ExpireCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("name"),
                                new RespBulkString("abc")
                        }
                );

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR invalid expire time",
                error.getMessage()
        );
    }

    @Test
    void shouldRejectNonPositiveExpiration() {

        DataStore store = new DataStore();

        ExpireCommand command =
                new ExpireCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("name"),
                                new RespBulkString("0")
                        }
                );

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR invalid expire time",
                error.getMessage()
        );
    }

    @Test
    void shouldRejectWrongNumberOfArguments() {

        DataStore store = new DataStore();

        ExpireCommand command =
                new ExpireCommand(store);

        RespValue response =
                command.execute(
                        new RespValue[]{
                                new RespBulkString("name")
                        }
                );

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR wrong number of arguments for 'expire' command",
                error.getMessage()
        );
    }
}