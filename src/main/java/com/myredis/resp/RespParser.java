package com.myredis.resp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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