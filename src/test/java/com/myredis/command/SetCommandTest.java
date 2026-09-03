package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class SetCommandTest {

    @Test
    void shouldStoreValue() {

        DataStore store = new DataStore();

        SetCommand command =
                new SetCommand(store);

        RespValue[] arguments = {
                new RespBulkString("name"),
                new RespBulkString("Lakshya")
        };

        RespValue response =
                command.execute(arguments);

        RespSimpleString simpleString =
                assertInstanceOf(
                        RespSimpleString.class,
                        response
                );

        assertEquals(
                "OK",
                simpleString.getValue()
        );

        assertEquals(
                "Lakshya",
                store.get("name")
        );
    }
    @Test
    void shouldRejectWrongNumberOfArguments() {
    
        DataStore store = new DataStore();
        SetCommand command = new SetCommand(store);
    
        RespValue[] arguments = {
                new RespBulkString("name")
        };
    
        RespValue response = command.execute(arguments);
    
        RespError error =
                assertInstanceOf(RespError.class, response);
    
        assertEquals(
                "ERR wrong number of arguments for 'set' command",
                error.getMessage()
        );
    }
    @Test
    void shouldRejectNonBulkStringKey() {
    
        DataStore store = new DataStore();
        SetCommand command = new SetCommand(store);
    
        RespValue[] arguments = {
                new RespSimpleString("name"),
                new RespBulkString("Lakshya")
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
    void shouldRejectNonBulkStringValue() {
    
        DataStore store = new DataStore();
        SetCommand command = new SetCommand(store);
    
        RespValue[] arguments = {
                new RespBulkString("name"),
                new RespInteger(100)
        };
    
        RespValue response = command.execute(arguments);
    
        RespError error =
                assertInstanceOf(RespError.class, response);
    
        assertEquals(
                "ERR value must be a bulk string",
                error.getMessage()
        );
    }
    @Test
    void shouldRejectNullKeyOrValue() {
    
        DataStore store = new DataStore();
        SetCommand command = new SetCommand(store);
    
        RespValue[] arguments = {
                new RespBulkString(null),
                new RespBulkString("Lakshya")
        };
    
        RespValue response = command.execute(arguments);
    
        RespError error =
                assertInstanceOf(RespError.class, response);
    
        assertEquals(
                "ERR key and value cannot be null",
                error.getMessage()
        );
    }
}