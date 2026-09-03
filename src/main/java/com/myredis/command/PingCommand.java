package com.myredis.command;

import com.myredis.resp.RespSimpleString;
import com.myredis.resp.RespValue;

public class PingCommand implements Command {

    @Override
    public RespValue execute(RespValue[] arguments) {
        return new RespSimpleString("PONG");
    }
}