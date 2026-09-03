package com.myredis.resp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RespEncoder {

    private final OutputStream output;

    public RespEncoder(OutputStream output) {
        this.output = output;
    }

    public void write(RespValue value) throws IOException {

        if (value instanceof RespSimpleString simpleString) {

            String response = "+" + simpleString.getValue() + "\r\n";

            output.write(response.getBytes(StandardCharsets.UTF_8));
            output.flush();

            return;
        }

        if (value instanceof RespInteger integer) {

            String response = ":" + integer.getValue() + "\r\n";

            output.write(response.getBytes(StandardCharsets.UTF_8));
            output.flush();

            return;
        }

        if (value instanceof RespError error) {

            String response = "-" + error.getMessage() + "\r\n";

            output.write(response.getBytes(StandardCharsets.UTF_8));
            output.flush();

            return;
        }

        if (value instanceof RespBulkString bulkString) {

            if (bulkString.getValue() == null) {

                String response = "$-1\r\n";

                output.write(response.getBytes(StandardCharsets.UTF_8));
                output.flush();

                return;
            }

            byte[] data =
                    bulkString.getValue().getBytes(StandardCharsets.UTF_8);

            String header = "$" + data.length + "\r\n";

            output.write(header.getBytes(StandardCharsets.UTF_8));
            output.write(data);
            output.write("\r\n".getBytes(StandardCharsets.UTF_8));
            output.flush();

            return;
        }

        if (value instanceof RespArray array) {

            String header = "*" + array.getValues().size() + "\r\n";

            output.write(header.getBytes(StandardCharsets.UTF_8));

            for (RespValue element : array.getValues()) {
                write(element);
            }

            return;
        }

        throw new IOException(
                "Unsupported RESP value: "
                        + value.getClass().getSimpleName()
        );
    }
}