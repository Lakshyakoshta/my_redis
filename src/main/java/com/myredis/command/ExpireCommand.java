package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

public class ExpireCommand implements Command {

    private final DataStore store;

    public ExpireCommand(DataStore store) {
        this.store = store;
    }

    @Override
    public RespValue execute(RespValue[] arguments) {

        if (arguments.length != 2) {
            return new RespError(
                    "ERR wrong number of arguments for 'expire' command"
            );
        }

        if (!(arguments[0] instanceof RespBulkString key)) {
            return new RespError(
                    "ERR key must be a bulk string"
            );
        }

        if (key.getValue() == null) {
            return new RespError(
                    "ERR key cannot be null"
            );
        }

        if (!(arguments[1] instanceof RespBulkString secondsValue)) {
            return new RespError(
                    "ERR invalid expire time"
            );
        }

        if (secondsValue.getValue() == null) {
            return new RespError(
                    "ERR invalid expire time"
            );
        }

        long seconds;

        try {
            seconds =
                    Long.parseLong(
                            secondsValue.getValue()
                    );
        } catch (NumberFormatException e) {
            return new RespError(
                    "ERR invalid expire time"
            );
        }

        if (seconds <= 0) {
            return new RespError(
                    "ERR invalid expire time"
            );
        }

        long ttlMillis =
                seconds * 1000;

        boolean expired =
                store.expire(
                        key.getValue(),
                        ttlMillis
                );

        return new RespInteger(
                expired ? 1 : 0
        );
    }
}