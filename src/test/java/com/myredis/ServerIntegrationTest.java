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

import com.myredis.resp.RespInteger;

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
    @Test
    void shouldHandleMultipleClients()
            throws Exception {
    
        try (
                Socket client1 =
                        new Socket("localhost", 6379);
    
                Socket client2 =
                        new Socket("localhost", 6379)
        ) {
    
            InputStream input1 =
                    client1.getInputStream();
    
            OutputStream output1 =
                    client1.getOutputStream();
    
            InputStream input2 =
                    client2.getInputStream();
    
            OutputStream output2 =
                    client2.getOutputStream();
    
            RespParser parser1 =
                    new RespParser(input1);
    
            RespParser parser2 =
                    new RespParser(input2);
    
            // Client 1
            output1.write(
                    (
                            "*3\r\n" +
                            "$3\r\n" +
                            "SET\r\n" +
                            "$3\r\n" +
                            "foo\r\n" +
                            "$3\r\n" +
                            "bar\r\n"
                    ).getBytes(StandardCharsets.UTF_8)
            );
    
            output1.flush();
    
            RespValue response1 =
                    parser1.parse();
    
            RespSimpleString result1 =
                    assertInstanceOf(
                            RespSimpleString.class,
                            response1
                    );
    
            assertEquals(
                    "OK",
                    result1.getValue()
            );
    
            // Client 2
            output2.write(
                    (
                            "*2\r\n" +
                            "$3\r\n" +
                            "GET\r\n" +
                            "$3\r\n" +
                            "foo\r\n"
                    ).getBytes(StandardCharsets.UTF_8)
            );
    
            output2.flush();
    
            RespValue response2 =
                    parser2.parse();
    
            RespBulkString result2 =
                    assertInstanceOf(
                            RespBulkString.class,
                            response2
                    );
    
            assertEquals(
                    "bar",
                    result2.getValue()
            );
        }
    }
    @Test
    void shouldHandleSecondClientWhileFirstClientIsIdle()
            throws Exception {
    
        try (
                Socket client1 =
                        new Socket("localhost", 6379)
        ) {
    
            // Client 1 connects but sends nothing.
            // The server should be waiting for input from
            // client 1.
    
            try (
                    Socket client2 =
                            new Socket("localhost", 6379)
            ) {
    
                InputStream input2 =
                        client2.getInputStream();
    
                OutputStream output2 =
                        client2.getOutputStream();
    
                RespParser parser2 =
                        new RespParser(input2);
    
                output2.write(
                        (
                                "*1\r\n" +
                                "$4\r\n" +
                                "PING\r\n"
                        ).getBytes(StandardCharsets.UTF_8)
                );
    
                output2.flush();
    
                RespValue response =
                        parser2.parse();
    
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
    }
    @Test
    void shouldHandleSetGetDeleteLifecycle()
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
            // DEL name
            // -------------------------
    
            String delRequest =
                    "*2\r\n" +
                    "$3\r\n" +
                    "DEL\r\n" +
                    "$4\r\n" +
                    "name\r\n";
    
            output.write(
                    delRequest.getBytes(StandardCharsets.UTF_8)
            );
    
            output.flush();
    
            RespValue delResponse =
                    parser.parse();
    
            RespInteger delResult =
                    assertInstanceOf(
                            RespInteger.class,
                            delResponse
                    );
    
            assertEquals(
                    1,
                    delResult.getValue()
            );
    
            // -------------------------
            // GET name again
            // -------------------------
    
            output.write(
                    getRequest.getBytes(StandardCharsets.UTF_8)
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