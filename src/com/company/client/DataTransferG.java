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

    public void sendForward(String forward) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("forward", forward);
        out.writeUTF(json.toString());
    }

    public String getFrame() throws IOException {
        String inStr = in.readUTF();
        JsonObject json = JsonParser.parseString(inStr).getAsJsonObject();
        System.out.println(inStr);
        return json.get("map").getAsString();
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
