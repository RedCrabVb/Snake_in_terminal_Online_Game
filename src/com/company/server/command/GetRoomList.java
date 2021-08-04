package com.company.server.command;

import com.company.DataTransfer;
import com.company.Main;
import com.google.gson.JsonObject;

import java.io.IOException;

public class GetRoomList implements Command {
    private final DataTransfer dataTransfer;

    public GetRoomList(DataTransfer dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    @Override
    public void execute(JsonObject json) {
        JsonObject jsonRoom = new JsonObject();
        jsonRoom.addProperty("listRoom", Main.getListRooms());
        try {
            dataTransfer.sendMessage(jsonRoom.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
