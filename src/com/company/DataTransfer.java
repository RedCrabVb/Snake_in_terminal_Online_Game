package com.company;

import java.io.*;
import java.net.Socket;

public class DataTransfer {
//    private final Socket socket;
    private final DataInput in;
    private final DataOutput out;

    public DataTransfer(Socket socket) throws IOException {
//        this.socket = socket;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public synchronized void sendMessage(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public synchronized String getMessage() throws IOException {
        String msg = in.readUTF();
        System.out.println(msg);
        return msg;
    }
}
