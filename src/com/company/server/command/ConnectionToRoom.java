package com.company.server.command;

import com.company.DataTransfer;
import com.company.server.Room;
import com.company.server.menu.MenuClient;
import com.google.gson.JsonObject;

import java.util.List;

public class ConnectionToRoom implements Command {
    private final DataTransfer dataTransfer;
    private final List<Room> roomList;
    private final MenuClient menuClient;

    public ConnectionToRoom(DataTransfer dataTransfer, List<Room> roomList, MenuClient menuClient) {
        this.dataTransfer = dataTransfer;
        this.roomList = roomList;
        this.menuClient = menuClient;
    }

    @Override
    public void execute(JsonObject json) {
        int numberRoom = json.get("numberRoom").getAsInt() - 1;
        try {
            try {
                roomList.get(numberRoom).addUser(dataTransfer, menuClient);
                menuClient.stopMenu();
                dataTransfer.sendMessage("{\"access\": " + "true" + "}");
            } catch (IndexOutOfBoundsException e) {
                dataTransfer.sendMessage("{\"access\": " + "false" + "}");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
