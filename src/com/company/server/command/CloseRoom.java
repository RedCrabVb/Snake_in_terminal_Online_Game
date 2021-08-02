package com.company.server.command;

import com.company.server.SnakeClient;
import com.google.gson.JsonObject;

public class CloseRoom implements Command {
    private SnakeClient snakeClient;

    public CloseRoom(SnakeClient snakeClient) {
        this.snakeClient = snakeClient;
    }

    @Override
    public void execute(JsonObject json) {
        snakeClient.close();
    }
}
