package com.myredis.resp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RespParser {

    private final InputStream input;

    public RespParser(InputStream input) {
        this.input = input;
    }

    public RespValue parse() throws IOException {

        int type = input.read();

        if (type == -1) {
            throw new IOException("Unexpected end of stream");
        }

        if (type == '+') {
            return new RespSimpleString(readLine());
        }

        if (type == ':') {
            String value = readLine();
            long number = Long.parseLong(value);
            return new RespInteger(number);
        }

        if (type == '-') {
            return new RespError(readLine());
        }

        if (type == '$') {
            int length = Integer.parseInt(readLine());

            if (length == -1) {
                return new RespBulkString(null);
            }

            byte[] data = new byte[length];

            int totalRead = 0;

            while (totalRead < length) {
                int bytesRead = input.read(data, totalRead, length - totalRead);

                if (bytesRead == -1) {
                    throw new IOException("Unexpected end of stream");
                }

                totalRead += bytesRead;
            }

            String value = new String(data, StandardCharsets.UTF_8);

            int cr = input.read();
            int lf = input.read();

            if (cr != '\r' || lf != '\n') {
                throw new IOException("Expected CRLF after bulk string");
            }

            return new RespBulkString(value);
        }
        if (type == '*') {
            int count = Integer.parseInt(readLine());

            List<RespValue> values = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                values.add(parse());
            }

            return new RespArray(values);
        }

        throw new IOException("Unsupported RESP type: " + (char) type);
    }
    

    private String readLine() throws IOException {

        StringBuilder builder = new StringBuilder();

        while (true) {

            int current = input.read();

            if (current == -1) {
                throw new IOException("Unexpected end of stream");
            }

            if (current == '\r') {

                int next = input.read();

                if (next != '\n') {
                    throw new IOException("Expected LF after CR");
                }

                break;
            }

            builder.append((char) current);
        }

        return builder.toString();
    }
}