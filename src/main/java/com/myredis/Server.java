package com.myredis;

import com.myredis.command.CommandDispatcher;
import com.myredis.resp.RespArray;
import com.myredis.resp.RespEncoder;
import com.myredis.resp.RespError;
import com.myredis.resp.RespParser;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Server {

    private final int port;

    private final DataStore store;

    private final CommandDispatcher dispatcher;

    private ServerSocket serverSocket;

    private final CountDownLatch startedLatch =
            new CountDownLatch(1);

    public Server(int port) {

        this.port = port;

        this.store = new DataStore();

        this.dispatcher =
                new CommandDispatcher(store);
    }

    public void start() throws IOException {

        serverSocket =
                new ServerSocket(port);

        startedLatch.countDown();

        System.out.println(
                "MyRedis server is listening on port " + port
        );

        while (!serverSocket.isClosed()) {

            Socket clientSocket =
                    serverSocket.accept();

            System.out.println("Client connected!");

            handleClient(clientSocket);
        }
    }

    public void stop() throws IOException {

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public void awaitStarted()
            throws InterruptedException {

        startedLatch.await();
    }

    private void handleClient(
            Socket clientSocket) {

        try (clientSocket) {

            InputStream input =
                    clientSocket.getInputStream();

            OutputStream output =
                    clientSocket.getOutputStream();

            RespParser parser =
                    new RespParser(input);

            RespEncoder encoder =
                    new RespEncoder(output);

            while (true) {

                RespValue request;

                try {
                    request = parser.parse();

                } catch (IOException e) {
                    break;
                }

                if (!(request instanceof RespArray array)) {

                    encoder.write(
                            new RespError(
                                    "ERR command must be an array"
                            )
                    );

                    continue;
                }

                RespValue response =
                        dispatcher.dispatch(array);

                encoder.write(response);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Server server =
                new Server(6379);

        try {
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}