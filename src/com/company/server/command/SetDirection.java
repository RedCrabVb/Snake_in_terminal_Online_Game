package com.company.server.command;

import com.company.server.SnakeClient;
import com.google.gson.JsonObject;

public class SetDirection implements Command {
    private SnakeClient snakeClient;

    public SetDirection(SnakeClient snakeClient) {
        this.snakeClient = snakeClient;
    }

    @Override
    public void execute(JsonObject json) {
        snakeClient.setMoveController(json.get("move").getAsString());
    }
}
