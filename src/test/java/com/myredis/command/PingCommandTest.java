package com.myredis.command;

import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PingCommandTest {

    @Test
    void shouldReturnPong() {

        Command command = new PingCommand();

        RespValue response = command.execute(new RespValue[0]);

        RespSimpleString simpleString =
                assertInstanceOf(RespSimpleString.class, response);

        assertEquals("PONG", simpleString.getValue());
    }
}