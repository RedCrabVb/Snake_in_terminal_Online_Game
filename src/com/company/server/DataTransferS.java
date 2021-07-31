package com.company.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class DataTransferS {
    private Socket socket;
    private DataInput in;
    private DataOutput out;

    public DataTransferS(Socket socket) throws IOException {
        this.socket = socket;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public String getForward() throws IOException {
        String inString = in.readUTF();
        JsonObject json = JsonParser.parseString(inString).getAsJsonObject();
        return json.get("forward").getAsString();
    }

    public void sendFrame(String map) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("map", map);
        out.writeUTF(json.toString());
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
