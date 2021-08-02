package com.company.server.menu;

import com.company.Main;
import com.company.dataBase.DataBase;
import com.company.dataBase.MySqlDataBase;
import com.company.server.Room;

import java.sql.SQLException;
import java.util.*;
import java.util.Scanner;

public class MenuServer extends Menu implements Runnable {
    private final List<Room> rooms;
    private String username;

    public MenuServer(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public synchronized void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username and password, " +
                "if a user with this name does not exist, " +
                "then it will be created, " +
                "if it exists and the password is wrong, " +
                "then there will be an error during registration");
        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!Main.dataBase.isRealAccount(username, password)) {
            System.out.println("False data");
            System.exit(0);
        }

        while (!Thread.currentThread().isInterrupted()) {
            if (isRunMenu()) {
                System.out.println("Enter menu item");
                System.out.println("1. Show list rooms");
                System.out.println("2. Connection to rooms");
                System.out.println("3. Create rooms");
                System.out.println("4. Users recorde");

                try {
                    String inputData = scanner.nextLine();

                    switch (inputData) {
                        case "1":
                            System.out.println(Main.getListRooms());
                            break;
                        case "2":
                            System.out.println("Enter number rooms");
                            int numberRoom = Integer.parseInt(scanner.nextLine()) - 1;
                            try {
                                rooms.get(numberRoom).addUser(this);
                                stopMenu();
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "3":
                            System.out.print("Enter name rooms: ");
                            rooms.add(new Room(scanner.nextLine()));
                            break;
                        case "4":
                            System.out.println(Main.dataBase.getRecorde());
                            break;
                        default:
                            System.out.println("Error enter data");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    wait();
                    startMenu();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("false run menu server");
            }
        }
    }

    public String getUsername() {
        return username;
    }
}