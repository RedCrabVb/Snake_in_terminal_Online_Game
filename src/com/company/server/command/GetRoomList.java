package com.company.server.command;

import com.company.DataTransfer;
import com.company.Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class GetRoomList implements Command {
    private List listRoom;
    private DataTransfer dataTransfer;
    private Gson gson;

    public GetRoomList(List listRoom, DataTransfer dataTransfer) {
        this.listRoom = listRoom;
        this.dataTransfer = dataTransfer;
        gson = new Gson();
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
