package com.myredis;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestClient {

    public static void main(String[] args) throws Exception {

        try (Socket socket = new Socket("localhost", 6379)) {

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            output.write("PING".getBytes(StandardCharsets.UTF_8));
            output.flush();

            byte[] buffer = new byte[1024];

            int bytesRead = input.read(buffer);

            String response = new String(
                    buffer,
                    0,
                    bytesRead,
                    StandardCharsets.UTF_8
            );

            System.out.println("Server response: " + response);
        }
    }
}