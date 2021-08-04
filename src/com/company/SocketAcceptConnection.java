package com.company;

import com.company.server.Room;
import com.company.server.menu.MenuClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketAcceptConnection implements Runnable {
    private final int port;
    private final List<Room> rooms;

    public SocketAcceptConnection(int port, List<Room> rooms) {
        this.port = port;
        this.rooms = rooms;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);

            while (!Thread.currentThread().isInterrupted()) {
                Socket socketClient = socket.accept();
                new Thread(new MenuClient(new DataTransfer(socketClient), rooms)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}