package com.company.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class DataTransferG {
    private final Socket socket;
    private final DataInput in;
    private final DataOutput out;

    public DataTransferG(Socket socket) throws IOException {
        this.socket = socket;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public synchronized void sendMessage(String msg) throws IOException {
        System.out.println(msg);
        out.writeUTF(msg);
    }

    public synchronized JsonObject getMessage() throws IOException {
        return JsonParser.parseString(in.readUTF()).getAsJsonObject();
    }

    public void sendForward(String forward) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("header", "SetDirection");
        json.addProperty("move", forward);
        sendMessage(json.toString());
    }

    public String getFrame() throws IOException {
        sendCommand("GetFrame");

        JsonObject jsonObj = getMessage();
        return jsonObj.get("frame").getAsString();
    }

    public String getListRoom() throws IOException {
        sendCommand("GetRoom");
        return getMessage().get("listRoom").getAsString();
    }

    public void connectionToRoom(int numberRoom) throws IOException {
        sendCommand("ConnectionToRoom", String.valueOf(numberRoom), "numberRoom");
    }

    public void sendCommand(String command) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("header", command);
        sendMessage(json.toString());
    }

    public void sendCommand(String command, String data, String nameField) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("header", command);
        json.addProperty(nameField, data);
        sendMessage(json.toString());
    }

    public boolean registration(String username, String password) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        sendMessage(json.toString());
        return getMessage().get("access").getAsBoolean();
    }

    public String getRecords() throws IOException {
        sendCommand("ShowRecords");
        return getMessage().get("showRecords").getAsString();
    }
}
