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

public class Server {

    public static void main(String[] args) {

        int port = 6379;

        DataStore store = new DataStore();

        CommandDispatcher dispatcher =
                new CommandDispatcher(store);

        try (ServerSocket serverSocket =
                     new ServerSocket(port)) {

            System.out.println(
                    "MyRedis server is listening on port " + port
            );

            while (true) {

                Socket clientSocket =
                        serverSocket.accept();

                System.out.println("Client connected!");

                try (clientSocket) {

                    InputStream input =
                            clientSocket.getInputStream();

                    OutputStream output =
                            clientSocket.getOutputStream();

                    RespParser parser =
                            new RespParser(input);

                    RespEncoder encoder =
                            new RespEncoder(output);

                    RespValue request =
                            parser.parse();

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
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}