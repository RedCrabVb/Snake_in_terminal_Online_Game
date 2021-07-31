package com.company.server.command;

import com.company.DataTransfer;
import com.company.server.SnakeClient;
import com.google.gson.JsonObject;

import java.io.IOException;

public class GetFrame implements Command {
    private DataTransfer dataTransfer;
    private SnakeClient snakeClient;

    public GetFrame(DataTransfer dataTransfer, SnakeClient snakeClient) {
        this.dataTransfer = dataTransfer;
        this.snakeClient = snakeClient;
    }

    @Override
    public void execute(JsonObject json) {
        try {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("frame", snakeClient.getFrame());
            dataTransfer.sendMessage(jsonObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
