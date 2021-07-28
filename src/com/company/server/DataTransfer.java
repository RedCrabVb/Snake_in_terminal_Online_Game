package com.company.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;

public class DataTransfer {
    private Socket socket;
    private DataInput in;
    private DataOutput out;

    public DataTransfer(Socket socket) throws IOException {
        this.socket = socket;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public String getForward() throws IOException {
        String inString = in.readUTF();
        JsonObject json = new JsonParser().parse(inString).getAsJsonObject();
        return json.get("forward").getAsString();
    }

    public void sendMap(String map) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("map", map);
        out.writeUTF(json.toString());
    }
}
