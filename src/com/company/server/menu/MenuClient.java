package com.company.server.menu;

import com.company.DataTransfer;
import com.company.server.Room;
import com.company.server.command.CommandSwitch;
import com.company.server.command.ConnectionToRoom;
import com.company.server.command.GetRoomList;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

public class MenuClient extends Menu implements Runnable {
    private volatile DataTransfer dataTransfer;
    private CommandSwitch commandSwitch;

    public MenuClient(DataTransfer dataTransfer, List<Room> roomList) {
        this.dataTransfer = dataTransfer;
        this.commandSwitch = new CommandSwitch();
        this.commandSwitch.register("GetRoom", new GetRoomList(dataTransfer));
        this.commandSwitch.register("ConnectionToRoom", new ConnectionToRoom(dataTransfer, roomList, this));
    }

    @Override
    public String getUsername() {
        return "client";
    }

    @Override
    public synchronized void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (isRunMenu()) {
                try {
                    commandSwitch.execute(dataTransfer.getMessage());
                } catch (EOFException e) {
                    e.printStackTrace();
                    break;
                }
                catch (IOException e) {
                    e.printStackTrace();
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