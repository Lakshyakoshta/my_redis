package com.myredis;

import com.myredis.command.CommandDispatcher;
import com.myredis.resp.RespArray;
import com.myredis.resp.RespEncoder;
import com.myredis.resp.RespParser;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerIntegrationTest {

    @Test
    void shouldProcessPingRequest() throws Exception {

        String request =
                "*1\r\n" +
                "$4\r\n" +
                "PING\r\n";

        ByteArrayInputStream input =
                new ByteArrayInputStream(
                        request.getBytes(StandardCharsets.UTF_8)
                );

        RespParser parser =
                new RespParser(input);

        RespValue parsedRequest =
                parser.parse();

        RespArray command =
                (RespArray) parsedRequest;

        CommandDispatcher dispatcher =
                new CommandDispatcher(new DataStore());

        RespValue response =
                dispatcher.dispatch(command);

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        RespEncoder encoder =
                new RespEncoder(output);

        encoder.write(response);

        String actualResponse =
                output.toString(StandardCharsets.UTF_8);

        assertEquals("+PONG\r\n", actualResponse);
    }
}