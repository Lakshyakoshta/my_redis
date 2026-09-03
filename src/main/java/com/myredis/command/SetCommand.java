package com.myredis.command;

import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

public class SetCommand implements Command {

    private final DataStore store;

    public SetCommand(DataStore store) {
        this.store = store;
    }

    @Override
    public RespValue execute(RespValue[] arguments) {

        if (arguments.length != 2) {
            return new RespError(
                    "ERR wrong number of arguments for 'set' command"
            );
        }

        if (!(arguments[0] instanceof RespBulkString key)) {
            return new RespError(
                    "ERR key must be a bulk string"
            );
        }

        if (!(arguments[1] instanceof RespBulkString value)) {
            return new RespError(
                    "ERR value must be a bulk string"
            );
        }

        if (key.getValue() == null || value.getValue() == null) {
            return new RespError(
                    "ERR key and value cannot be null"
            );
        }

        store.set(
                key.getValue(),
                value.getValue()
        );

        return new RespSimpleString("OK");
    }
}