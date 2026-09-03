package com.myredis.resp;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RespEncoderTest {

    @Test
    void shouldEncodeSimpleString() throws Exception {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        RespEncoder encoder =
                new RespEncoder(output);

        encoder.write(new RespSimpleString("PONG"));

        String result =
                output.toString(StandardCharsets.UTF_8);

        assertEquals("+PONG\r\n", result);
    }
    @Test
    void shouldEncodeInteger() throws Exception {
    
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
    
        RespEncoder encoder =
                new RespEncoder(output);
    
        encoder.write(new RespInteger(100));
    
        String result =
                output.toString(StandardCharsets.UTF_8);
    
        assertEquals(":100\r\n", result);
    }
    @Test
    void shouldEncodeError() throws Exception {
    
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
    
        RespEncoder encoder =
                new RespEncoder(output);
    
        encoder.write(new RespError("ERR unknown command"));
    
        String result =
                output.toString(StandardCharsets.UTF_8);
    
        assertEquals("-ERR unknown command\r\n", result);
    }
    @Test
    void shouldEncodeBulkString() throws Exception {
    
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
    
        RespEncoder encoder =
                new RespEncoder(output);
    
        encoder.write(new RespBulkString("hello"));
    
        String result =
                output.toString(StandardCharsets.UTF_8);
    
        assertEquals("$5\r\nhello\r\n", result);
    }
    @Test
    void shouldEncodeNullBulkString() throws Exception {
    
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
    
        RespEncoder encoder =
                new RespEncoder(output);
    
        encoder.write(new RespBulkString(null));
    
        String result =
                output.toString(StandardCharsets.UTF_8);
    
        assertEquals("$-1\r\n", result);
    }
    @Test
    void shouldEncodeArray() throws Exception {
    
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
    
        RespEncoder encoder =
                new RespEncoder(output);
    
        RespArray array =
                new RespArray(
                        List.of(
                                new RespBulkString("GET"),
                                new RespBulkString("name")
                        )
                );
    
        encoder.write(array);
    
        String result =
                output.toString(StandardCharsets.UTF_8);
    
        assertEquals(
                "*2\r\n" +
                "$3\r\n" +
                "GET\r\n" +
                "$4\r\n" +
                "name\r\n",
                result
        );
    }
    @Test
    void shouldEncodeEmptyArray() throws Exception {
    
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
    
        RespEncoder encoder =
                new RespEncoder(output);
    
        RespArray array =
                new RespArray(List.of());
    
        encoder.write(array);
    
        String result =
                output.toString(StandardCharsets.UTF_8);
    
        assertEquals("*0\r\n", result);
    }
}