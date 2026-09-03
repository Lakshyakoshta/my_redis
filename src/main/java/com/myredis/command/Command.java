package com.myredis.command;

import com.myredis.resp.RespValue;

public interface Command {

    RespValue execute(RespValue[] arguments);
}