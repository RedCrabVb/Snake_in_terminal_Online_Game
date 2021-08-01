package com.company.server.command;

import com.company.DataTransfer;
import com.company.server.Room;
import com.company.server.menu.MenuClient;
import com.google.gson.JsonObject;

import java.util.List;

public class ConnectionToRoom implements Command {
    private DataTransfer dataTransfer;
    private List<Room> roomList;
    private MenuClient menuClient;

    public ConnectionToRoom(DataTransfer dataTransfer, List<Room> roomList, MenuClient menuClient) {
        this.dataTransfer = dataTransfer;
        this.roomList = roomList;
        this.menuClient = menuClient;
    }

    @Override
    public void execute(JsonObject json) {
        int numberRoom = json.get("numberRoom").getAsInt() - 1;
        roomList.get(numberRoom).addUser(dataTransfer, menuClient);
        menuClient.stopMenu();
    }
}
