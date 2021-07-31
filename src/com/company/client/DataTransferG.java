package com.company.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class DataTransferG {
    private Socket socket;
    private DataInput in;
    private DataOutput out;

    public DataTransferG(Socket socket) throws IOException {
        this.socket = socket;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public String getMessage() throws IOException {
        return in.readUTF();
    }

    public void sendForward(String forward) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("header", "SetDirection");
        json.addProperty("move", forward);

        sendMessage(json.toString());
    }

    public String getFrame() throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("header", "GetFrame");

        String inStr = getMessage();
        JsonObject jsonObj = JsonParser.parseString(inStr).getAsJsonObject();
        return jsonObj.get("GetFrame").getAsString();
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isClose() {
        return socket.isClosed();
    }
}
