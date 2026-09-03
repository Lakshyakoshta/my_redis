package com.myredis.command;

import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

import org.junit.jupiter.api.Test;

import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespArray;
import com.myredis.resp.RespBulkString;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class CommandDispatcherTest {

    @Test
    void shouldReturnErrorForUnknownCommand() {
    
        CommandDispatcher dispatcher =
                new CommandDispatcher(new DataStore());
    
        RespArray request =
            new RespArray(
                    List.of(
                        new RespBulkString("UNKNOWN")
                    )
            );

        RespValue response =
                dispatcher.dispatch(request);
    
        RespError error =
                assertInstanceOf(RespError.class, response);
    
        assertEquals(
                "ERR unknown command 'UNKNOWN'",
                error.getMessage()
        );
    }
    @Test
    void shouldDispatchPing() {
    
        CommandDispatcher dispatcher =
                new CommandDispatcher(new DataStore());
    
        RespArray request =
                new RespArray(
                        List.of(
                                new RespBulkString("PING")
                        )
                );
    
        RespValue response =
                dispatcher.dispatch(request);
    
        RespSimpleString simpleString =
                assertInstanceOf(
                        RespSimpleString.class,
                        response
                );
    
        assertEquals("PONG", simpleString.getValue());
    }
    @Test
    void shouldRejectEmptyCommand() {
    
        CommandDispatcher dispatcher =
                new CommandDispatcher(new DataStore());
    
        RespArray request =
                new RespArray(List.of());
    
        RespValue response =
                dispatcher.dispatch(request);
    
        RespError error =
                assertInstanceOf(RespError.class, response);
    
        assertEquals(
                "ERR empty command",
                error.getMessage()
        );
    }
    @Test
    void shouldDispatchGet() {
    
        DataStore store = new DataStore();
    
        store.set("name", "Lakshya");
    
        CommandDispatcher dispatcher =
                new CommandDispatcher(store);
    
        RespArray request =
                new RespArray(
                        List.of(
                                new RespBulkString("GET"),
                                new RespBulkString("name")
                        )
                );
    
        RespValue response =
                dispatcher.dispatch(request);
    
        RespBulkString bulkString =
                assertInstanceOf(RespBulkString.class, response);
    
        assertEquals(
                "Lakshya",
                bulkString.getValue()
        );
    }
    @Test
    void shouldDispatchDel() {
    
        DataStore store =
                new DataStore();
    
        store.set("name", "Lakshya");
    
        CommandDispatcher dispatcher =
                new CommandDispatcher(store);
    
        RespArray request =
                new RespArray(
                        List.of(
                                new RespBulkString("DEL"),
                                new RespBulkString("name")
                        )
                );
    
        RespValue response =
                dispatcher.dispatch(request);
    
        RespInteger result =
                assertInstanceOf(
                        RespInteger.class,
                        response
                );
    
        assertEquals(
                1,
                result.getValue()
        );
    
        assertNull(
                store.get("name")
        );
    }
}