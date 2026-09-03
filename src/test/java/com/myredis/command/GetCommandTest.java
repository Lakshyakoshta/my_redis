package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetCommandTest {

    @Test
    void shouldReturnStoredValue() {

        DataStore store = new DataStore();

        store.set("name", "Lakshya");

        GetCommand command = new GetCommand(store);

        RespValue[] arguments = {
                new RespBulkString("name")
        };

        RespValue response = command.execute(arguments);

        RespBulkString bulkString =
                assertInstanceOf(RespBulkString.class, response);

        assertEquals("Lakshya", bulkString.getValue());
    }

    @Test
    void shouldReturnNullWhenKeyDoesNotExist() {

        DataStore store = new DataStore();

        GetCommand command = new GetCommand(store);

        RespValue[] arguments = {
                new RespBulkString("unknown")
        };

        RespValue response = command.execute(arguments);

        RespBulkString bulkString =
                assertInstanceOf(RespBulkString.class, response);

        assertNull(bulkString.getValue());
    }

    @Test
    void shouldRejectWrongNumberOfArguments() {

        DataStore store = new DataStore();

        GetCommand command = new GetCommand(store);

        RespValue[] arguments = {};

        RespValue response = command.execute(arguments);

        RespError error =
                assertInstanceOf(RespError.class, response);

        assertEquals(
                "ERR wrong number of arguments for 'get' command",
                error.getMessage()
        );
    }

    @Test
    void shouldRejectNonBulkStringKey() {

        DataStore store = new DataStore();

        GetCommand command = new GetCommand(store);

        RespValue[] arguments = {
                new RespSimpleString("name")
        };

        RespValue response = command.execute(arguments);

        RespError error =
                assertInstanceOf(RespError.class, response);

        assertEquals(
                "ERR key must be a bulk string",
                error.getMessage()
        );
    }

    @Test
    void shouldRejectNullKey() {

        DataStore store = new DataStore();

        GetCommand command = new GetCommand(store);

        RespValue[] arguments = {
                new RespBulkString(null)
        };

        RespValue response = command.execute(arguments);

        RespError error =
                assertInstanceOf(RespError.class, response);

        assertEquals(
                "ERR key cannot be null",
                error.getMessage()
        );
    }
}