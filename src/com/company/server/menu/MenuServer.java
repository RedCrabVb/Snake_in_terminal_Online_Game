package com.company.server.menu;

import com.company.Main;
import com.company.server.Room;

import java.util.*;
import java.util.Scanner;

public class MenuServer extends  Thread {
    private List<Room> rooms;

    public MenuServer(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter menu item");
            System.out.println("1. Show list rooms");
            System.out.println("2. Connection to rooms");
            System.out.println("3. Create rooms");

            try {
                String inputData = scanner.nextLine();

                switch (inputData) {
                    case "1":
                        System.out.println(Main.getListRooms());
                        break;
                    case "2":
                        System.out.println("Enter number rooms");
                        int numberRoom = Integer.parseInt(scanner.nextLine()) - 1;
                        rooms.get(numberRoom).addUser();
                        break;
                    case "3":
                        System.out.print("Enter name rooms: ");
                        rooms.add(new Room(scanner.nextLine()));
                        break;
                    default:
                        System.out.println("Error enter data");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}