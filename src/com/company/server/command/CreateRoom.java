package com.company.server.command;

import com.company.server.Room;
import com.google.gson.JsonObject;

import java.util.List;

public class CreateRoom implements Command {
    private final List<Room> roomList;

    public CreateRoom(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public void execute(JsonObject json) {
        roomList.add(new Room(json.get("nameRoom").getAsString()));
    }
}
