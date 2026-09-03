package com.myredis.command;

import com.myredis.resp.RespArray;
import com.myredis.resp.RespBulkString;
import com.myredis.resp.RespError;
import com.myredis.resp.RespValue;
import com.myredis.store.DataStore;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {

    private final Map<String, Command> commands =
            new HashMap<>();

    public CommandDispatcher(DataStore store) {

        commands.put("PING", new PingCommand());
        commands.put("SET", new SetCommand(store));
        commands.put("GET", new GetCommand(store));
        commands.put("DEL", new DelCommand(store));
    }

    public RespValue dispatch(RespArray request) {

        if (request.getValues().isEmpty()) {
            return new RespError("ERR empty command");
        }

        RespValue commandValue =
                request.getValues().get(0);

        if (!(commandValue instanceof RespBulkString bulkString)) {
            return new RespError(
                    "ERR command must be a bulk string"
            );
        }

        if (bulkString.getValue() == null) {
            return new RespError(
                    "ERR command cannot be null"
            );
        }

        String commandName =
                bulkString.getValue().toUpperCase();

        Command command =
                commands.get(commandName);

        if (command == null) {
            return new RespError(
                    "ERR unknown command '" + commandName + "'"
            );
        }

        RespValue[] arguments =
                request.getValues()
                        .subList(1, request.getValues().size())
                        .toArray(new RespValue[0]);

        return command.execute(arguments);
    }
}