package com.myredis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) {

        int port = 6379;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Our Redis server is listening on port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected!");

                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                byte[] buffer = new byte[1024];

                int bytesRead = input.read(buffer);

                String request = new String(
                        buffer,
                        0,
                        bytesRead,
                        StandardCharsets.UTF_8
                );

                System.out.println("Received: " + request);

                output.write("PONG\r\n".getBytes(StandardCharsets.UTF_8));
                output.flush();

                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
