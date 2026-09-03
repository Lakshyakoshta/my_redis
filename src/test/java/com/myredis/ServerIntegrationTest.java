package com.myredis;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespParser;
import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServerIntegrationTest {

    private Server server;

    private Thread serverThread;

    @BeforeEach
    void startServer() throws Exception {

        server =
                new Server(6379);

        serverThread =
                new Thread(() -> {

                    try {
                        server.start();

                    } catch (IOException e) {

                        if (!serverThread.isInterrupted()) {
                            e.printStackTrace();
                        }
                    }
                });

        serverThread.start();

        server.awaitStarted();
    }

    @AfterEach
    void stopServer() throws Exception {

        server.stop();

        serverThread.interrupt();
    }

    @Test
    void shouldHandlePing() throws Exception {

        try (Socket socket =
                     new Socket("localhost", 6379)) {

            InputStream input =
                    socket.getInputStream();

            OutputStream output =
                    socket.getOutputStream();

            RespParser parser =
                    new RespParser(input);

            String request =
                    "*1\r\n" +
                    "$4\r\n" +
                    "PING\r\n";

            output.write(
                    request.getBytes(StandardCharsets.UTF_8)
            );

            output.flush();

            RespValue response =
                    parser.parse();

            RespSimpleString result =
                    assertInstanceOf(
                            RespSimpleString.class,
                            response
                    );

            assertEquals(
                    "PONG",
                    result.getValue()
            );
        }
    }

    @Test
    void shouldHandleMultipleCommandsOnSameConnection()
            throws Exception {

        try (Socket socket =
                     new Socket("localhost", 6379)) {

            InputStream input =
                    socket.getInputStream();

            OutputStream output =
                    socket.getOutputStream();

            RespParser parser =
                    new RespParser(input);

            // -------------------------
            // SET name Lakshya
            // -------------------------

            String setRequest =
                    "*3\r\n" +
                    "$3\r\n" +
                    "SET\r\n" +
                    "$4\r\n" +
                    "name\r\n" +
                    "$7\r\n" +
                    "Lakshya\r\n";

            output.write(
                    setRequest.getBytes(StandardCharsets.UTF_8)
            );

            output.flush();

            RespValue setResponse =
                    parser.parse();

            RespSimpleString setResult =
                    assertInstanceOf(
                            RespSimpleString.class,
                            setResponse
                    );

            assertEquals(
                    "OK",
                    setResult.getValue()
            );

            // -------------------------
            // GET name
            // -------------------------

            String getRequest =
                    "*2\r\n" +
                    "$3\r\n" +
                    "GET\r\n" +
                    "$4\r\n" +
                    "name\r\n";

            output.write(
                    getRequest.getBytes(StandardCharsets.UTF_8)
            );

            output.flush();

            RespValue getResponse =
                    parser.parse();

            RespBulkString getResult =
                    assertInstanceOf(
                            RespBulkString.class,
                            getResponse
                    );

            assertEquals(
                    "Lakshya",
                    getResult.getValue()
            );

            // -------------------------
            // GET missing key
            // -------------------------

            String missingRequest =
                    "*2\r\n" +
                    "$3\r\n" +
                    "GET\r\n" +
                    "$3\r\n" +
                    "age\r\n";

            output.write(
                    missingRequest.getBytes(StandardCharsets.UTF_8)
            );

            output.flush();

            RespValue missingResponse =
                    parser.parse();

            RespBulkString missingResult =
                    assertInstanceOf(
                            RespBulkString.class,
                            missingResponse
                    );

            assertNull(
                    missingResult.getValue()
            );
        }
    }
}