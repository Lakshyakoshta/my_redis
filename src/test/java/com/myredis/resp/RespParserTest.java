package com.myredis.resp;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RespParserTest {

    @Test
    void shouldParseSimpleString() throws Exception {

        String input = "+OK\r\n";

        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );

        RespParser parser = new RespParser(stream);

        RespValue value = parser.parse();

        RespSimpleString simpleString = (RespSimpleString) value;

        assertEquals("OK", simpleString.getValue());
    }

    @Test
    void shouldParseInteger() throws Exception {

        String input = ":100\r\n";

        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );

        RespParser parser = new RespParser(stream);

        RespValue value = parser.parse();

        RespInteger integer = (RespInteger) value;

        assertEquals(100, integer.getValue());
    }

    @Test
    void shouldParseError() throws Exception {
    
        String input = "-ERR unknown command\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespError error = (RespError) value;
    
        assertEquals("ERR unknown command", error.getMessage());
    }
    @Test
    void shouldParseBulkString() throws Exception {
    
        String input = "$5\r\nhello\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespBulkString bulkString = (RespBulkString) value;
    
        assertEquals("hello", bulkString.getValue());
    }
    @Test
    void shouldParseEmptyBulkString() throws Exception {
    
        String input = "$0\r\n\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespBulkString bulkString = (RespBulkString) value;
    
        assertEquals("", bulkString.getValue());
    }
    @Test
    void shouldParseBulkStringWithMultipleWords() throws Exception {
    
        String input = "$11\r\nhello world\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespBulkString bulkString = (RespBulkString) value;
    
        assertEquals("hello world", bulkString.getValue());
    }
}