package com.company.server.menu;

import com.company.DataTransfer;
import com.company.server.Room;
import com.company.server.command.CommandSwitch;
import com.company.server.command.ConnectionToRoom;
import com.company.server.command.GetRoomList;

import java.io.IOException;
import java.util.List;

public class MenuClient extends Thread {
    private DataTransfer dataTransfer;
    private CommandSwitch commandSwitch;
    private boolean isRanServer = true;

    public MenuClient(DataTransfer dataTransfer, List<Room> roomList) {
        this.dataTransfer = dataTransfer;
        this.commandSwitch = new CommandSwitch();
        this.commandSwitch.register("GetRoomList", new GetRoomList(roomList, dataTransfer));
        this.commandSwitch.register("ConnectionToRoom", new ConnectionToRoom(dataTransfer, roomList, this));
    }

    @Override
    public void run() {
        while (isRanServer) {
            try {
                commandSwitch.execute(dataTransfer.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void stopServer() {
        isRanServer = false;
    }
}