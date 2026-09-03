package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

class DelCommandTest {

    @Test
    void shouldDeleteExistingKey() {

        DataStore store =
                new DataStore();

        store.set("name", "Lakshya");

        DelCommand command =
                new DelCommand(store);

        RespValue[] arguments = {
                new RespBulkString("name")
        };

        RespValue response =
                command.execute(arguments);

        RespInteger result =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertEquals(
                1,
                result.getValue()
        );

        assertNull(store.get("name")); 
    }

    @Test
    void shouldReturnZeroForMissingKey() {

        DataStore store =
                new DataStore();

        DelCommand command =
                new DelCommand(store);

        RespValue[] arguments = {
                new RespBulkString("unknown")
        };

        RespValue response =
                command.execute(arguments);

        RespInteger result =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );

        assertEquals(
                0,
                result.getValue()
        );
    }

    @Test
    void shouldRejectWrongNumberOfArguments() {

        DataStore store =
                new DataStore();

        DelCommand command =
                new DelCommand(store);

        RespValue response =
                command.execute(new RespValue[0]);

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR wrong number of arguments for 'del' command",
                error.getMessage()
        );
    }

    @Test
    void shouldRejectNonBulkStringKey() {

        DataStore store =
                new DataStore();

        DelCommand command =
                new DelCommand(store);

        RespValue[] arguments = {
                new RespSimpleString("name")
        };

        RespValue response =
                command.execute(arguments);

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR key must be a bulk string",
                error.getMessage()
        );
    }

    @Test
    void shouldRejectNullKey() {

        DataStore store =
                new DataStore();

        DelCommand command =
                new DelCommand(store);

        RespValue[] arguments = {
                new RespBulkString(null)
        };

        RespValue response =
                command.execute(arguments);

        RespError error =
                assertInstanceOf(
                        RespError.class,
                        response
                );

        assertEquals(
                "ERR key cannot be null",
                error.getMessage()
        );
    }
}