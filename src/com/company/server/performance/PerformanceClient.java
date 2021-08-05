package com.company.server.performance;

import com.company.DataTransfer;
import com.company.server.SocketAcceptConnection;
import com.company.server.Room;
import com.company.server.command.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

public class PerformanceClient implements Runnable, Performance {
    private final CommandSwitch commandSwitch;
    private volatile DataTransfer dataTransfer;
    private String username;

    public PerformanceClient(DataTransfer dataTransfer, List<Room> roomList) {
        this.dataTransfer = dataTransfer;
        this.commandSwitch = new CommandSwitch();
        this.commandSwitch.register("GetRoom", new GetRoomList(dataTransfer));
        this.commandSwitch.register("ConnectionToRoom", new ConnectionToRoom(dataTransfer, roomList, this));
        this.commandSwitch.register("CreateRoom", new CreateRoom(roomList));
        this.commandSwitch.register("ShowRecords", new ShowRecords(dataTransfer));
    }

    @Override
    public void runMenu() {
        notifyAll();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public synchronized void run() {
        try {
            JsonObject regJson = JsonParser.parseString(dataTransfer.getMessage()).getAsJsonObject();
            username = regJson.get("username").getAsString();
            boolean reg = SocketAcceptConnection.dataBase.isRealAccount(username, regJson.get("password").getAsString());
            JsonObject jsonAnswer = new JsonObject();
            jsonAnswer.addProperty("access", reg);
            dataTransfer.sendMessage(jsonAnswer.toString());
            if (!reg) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (!Thread.currentThread().isInterrupted()) {
            try {
                commandSwitch.execute(dataTransfer.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}