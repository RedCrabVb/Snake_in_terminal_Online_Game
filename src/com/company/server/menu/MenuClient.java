package com.company.server.menu;

import com.company.DataTransfer;
import com.company.server.Room;
import com.company.server.command.CommandSwitch;
import com.company.server.command.ConnectionToRoom;
import com.company.server.command.GetRoomList;

import java.io.IOException;
import java.util.List;

public class MenuClient extends Menu implements Runnable {
    private DataTransfer dataTransfer;
    private CommandSwitch commandSwitch;

    public MenuClient(DataTransfer dataTransfer, List<Room> roomList) {
        this.dataTransfer = dataTransfer;
        this.commandSwitch = new CommandSwitch();
        this.commandSwitch.register("GetRoom", new GetRoomList(dataTransfer));
        this.commandSwitch.register("ConnectionToRoom", new ConnectionToRoom(dataTransfer, roomList, this));
    }

    @Override
    public synchronized void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (isRunMenu()) {
                try {
                    commandSwitch.execute(dataTransfer.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } else {
                try {
                    wait();
                    startMenu();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}