package com.company.server.command;

import com.company.DataTransfer;
import com.company.SocketAcceptConnection;
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
        jsonRoom.addProperty("listRoom", SocketAcceptConnection.getListRooms());
        try {
            dataTransfer.sendMessage(jsonRoom.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
