package com.company;

import com.company.client.DataTransferG;
import com.company.client.SnakeClientG;
import com.company.server.Room;
import com.company.server.menu.MenuClient;
import com.company.server.menu.MenuServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static List<Room> rooms = new ArrayList();

    public static String getListRooms() {
        var ref = new Object() {
            String print = "";
        };
        ref.print += "\nRooms:\n";
        AtomicInteger id = new AtomicInteger(0);
        rooms.forEach(r -> ref.print += (id.incrementAndGet() + " " + r.toString()) + "\n");
        return ref.print;
    }

    public static void removeRoom(Room room) {
        rooms.remove(room);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 or 2: \n 1) Create server \n 2) Connect server");
        String selection = scanner.nextLine();
        if (selection.equals("1")) {
            System.out.println("Enter port for program");
            String port = scanner.nextLine();

            new Thread(new MenuServer(rooms)).start();

            ServerSocket socket = new ServerSocket(Integer.parseInt(port));
            while (true) {
                Socket socketClient = socket.accept();
                new Thread(new MenuClient(new DataTransfer(socketClient), rooms)).start();
            }

        } else if (selection.equals("2")) {
            System.out.println("Enter ip server");
            String ip = "127.0.0.1";//scanner.nextLine();
            System.out.println("Enter port server");
            String port = scanner.nextLine();

            SnakeClientG snakeClientGame = new SnakeClientG(new DataTransferG(new Socket(ip, Integer.parseInt(port))));
            snakeClientGame.start();
        } else {
            throw new IOException("Error enter data");
        }
    }

}
