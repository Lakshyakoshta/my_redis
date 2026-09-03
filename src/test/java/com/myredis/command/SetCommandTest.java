package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;
import org.junit.jupiter.api.Test;

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
}