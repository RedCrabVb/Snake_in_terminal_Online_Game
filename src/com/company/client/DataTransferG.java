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

    public void sendForward(String forward) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("forward", forward);
        out.writeUTF(json.toString());
    }

    public String getMap() throws Exception {
        String inStr = in.readUTF();
        JsonObject json = new JsonParser().parse(inStr).getAsJsonObject();
        return json.get("map").getAsString();
    }
}
