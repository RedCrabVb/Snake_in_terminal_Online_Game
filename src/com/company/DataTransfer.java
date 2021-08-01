package com.company;

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

    public void sendMessage(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public String getMessage() throws IOException {
        String msg = in.readUTF();
        System.out.println(msg);
        return msg;
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
