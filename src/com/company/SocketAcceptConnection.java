package com.company;

import com.company.dataBase.DataBase;
import com.company.server.Room;
import com.company.server.menu.Performance;
import com.company.server.menu.PerformanceClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketAcceptConnection implements Runnable {
    private static final List<Room> rooms = new ArrayList<>();
    public static DataBase dataBase;
    private final int port;

    public SocketAcceptConnection(int port) {
        this.port = port;
    }

    public static String getListRooms() {
        var ref = new Object() {
            String print = "";
        };
        ref.print += "\nRooms:\n";
        AtomicInteger id = new AtomicInteger(0);
        rooms.forEach(r -> ref.print += (id.incrementAndGet() + " " + r.toString()) + "\n");
        return ref.print;
    }

    public static void addRooms(String nameRoom) {
        rooms.add(new Room(nameRoom));
    }

    public static void removeRooms(Room room) {
        rooms.remove(room);
    }

    public static void addUserToRoom(int numberRoom, Performance menu) {
        rooms.get(numberRoom).addUser(menu);
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);

            while (!Thread.currentThread().isInterrupted()) {
                Socket socketClient = socket.accept();
                new Thread(new PerformanceClient(new DataTransfer(socketClient), rooms)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}