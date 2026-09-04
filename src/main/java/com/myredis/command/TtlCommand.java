package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespInteger;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

public class TtlCommand implements Command {

    private final DataStore store;

    public TtlCommand(DataStore store) {
        this.store = store;
    }

    @Override
    public RespValue execute(RespValue[] arguments) {

        if (arguments.length != 1) {
            return new RespError(
                    "ERR wrong number of arguments for 'ttl' command"
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

        long ttl =
                store.ttl(key.getValue());

        return new RespInteger(ttl);
    }
}