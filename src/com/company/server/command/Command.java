package com.company.server.command;

import com.google.gson.JsonObject;

public interface Command {
    void execute(JsonObject json);
}
