package com.myredis;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestClient {

    public static void main(String[] args) throws Exception {

        try (Socket socket =
                     new Socket("localhost", 6379)) {

            InputStream input =
                    socket.getInputStream();

            OutputStream output =
                    socket.getOutputStream();

            sendCommand(
                    output,
                    "*1\r\n" +
                    "$4\r\n" +
                    "PING\r\n"
            );

            readResponse(input);

            sendCommand(
                    output,
                    "*3\r\n" +
                    "$3\r\n" +
                    "SET\r\n" +
                    "$4\r\n" +
                    "name\r\n" +
                    "$7\r\n" +
                    "Lakshya\r\n"
            );

            readResponse(input);

            sendCommand(
                    output,
                    "*2\r\n" +
                    "$3\r\n" +
                    "GET\r\n" +
                    "$4\r\n" +
                    "name\r\n"
            );

            readResponse(input);

            sendCommand(
                    output,
                    "*2\r\n" +
                    "$3\r\n" +
                    "GET\r\n" +
                    "$3\r\n" +
                    "age\r\n"
            );

            readResponse(input);
        }
    }

    private static void sendCommand(
            OutputStream output,
            String request) throws Exception {

        output.write(
                request.getBytes(StandardCharsets.UTF_8)
        );

        output.flush();
    }

    private static void readResponse(
            InputStream input) throws Exception {

        byte[] buffer = new byte[1024];

        int bytesRead =
                input.read(buffer);

        String response =
                new String(
                        buffer,
                        0,
                        bytesRead,
                        StandardCharsets.UTF_8
                );

        System.out.println(
                "Server response: " + response
        );
    }
}