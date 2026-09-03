package com.myredis.resp;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    @Test
    void shouldParseArray() throws Exception {
    
        String input =
                "*2\r\n" +
                "$3\r\n" +
                "GET\r\n" +
                "$4\r\n" +
                "name\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespArray array = (RespArray) value;
    
        assertEquals(2, array.getValues().size());
    
        RespBulkString first =
                (RespBulkString) array.getValues().get(0);
    
        RespBulkString second =
                (RespBulkString) array.getValues().get(1);
    
        assertEquals("GET", first.getValue());
        assertEquals("name", second.getValue());
    }
    @Test
    void shouldParseEmptyArray() throws Exception {
    
        String input = "*0\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespArray array = (RespArray) value;
    
        assertEquals(0, array.getValues().size());
    }
    @Test
    void shouldParseNegativeInteger() throws Exception {
    
        String input = ":-42\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespInteger integer = (RespInteger) value;
    
        assertEquals(-42, integer.getValue());
    }
    @Test
    void shouldParseNestedArray() throws Exception {
    
        String input =
                "*2\r\n" +
                "*2\r\n" +
                ":1\r\n" +
                ":2\r\n" +
                "+OK\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespArray outerArray = (RespArray) parser.parse();
    
        assertEquals(2, outerArray.getValues().size());
    
        RespArray innerArray =
                (RespArray) outerArray.getValues().get(0);
    
        assertEquals(2, innerArray.getValues().size());
    
        RespInteger first =
                (RespInteger) innerArray.getValues().get(0);
    
        RespInteger second =
                (RespInteger) innerArray.getValues().get(1);
    
        assertEquals(1, first.getValue());
        assertEquals(2, second.getValue());
    
        RespSimpleString message =
                (RespSimpleString) outerArray.getValues().get(1);
    
        assertEquals("OK", message.getValue());
    }
    @Test
    void shouldParseNullBulkString() throws Exception {
    
        String input = "$-1\r\n";
    
        ByteArrayInputStream stream =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                );
    
        RespParser parser = new RespParser(stream);
    
        RespValue value = parser.parse();
    
        RespBulkString bulkString = (RespBulkString) value;
    
        assertNull(bulkString.getValue());
    }
}