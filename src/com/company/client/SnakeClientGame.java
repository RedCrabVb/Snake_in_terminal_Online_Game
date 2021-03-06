package com.company.client;

import com.company.Config;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Scanner;

public class SnakeClientGame extends Thread {
/*    private volatile String frame = "";
    private volatile DataTransferGame dataTransfer;
    private volatile String control = "a";
    private volatile boolean gameIsActive = false;*/

    @Override
    public void run() {
/*        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username and password, " +
                "if a user with this name does not exist, " +
                "then it will be created, " +
                "if it exists and the password is wrong, " +
                "then there will be an error during registration");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean result = false;
        try {
            result = dataTransfer.registration(username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!result) {
            System.out.println("False input data");
            System.exit(0);
        }


        while (true) {
            System.out.println("Enter menu item");
            System.out.println("1. Show list rooms");
            System.out.println("2. Connection to rooms");
            System.out.println("3. Create rooms");
            System.out.println("4. Users recorde");
            try {
                String inputData = scanner.nextLine();
                switch (inputData) {
                    case "1":
                        System.out.println("get list room");
                        System.out.println(dataTransfer.getListRoom());

                        break;
                    case "2":
                        System.out.println("Enter number room");
                        int numberRoom = scanner.nextInt();
                        dataTransfer.connectionToRoom(numberRoom);
                        JsonObject json = dataTransfer.getMessage();
                        if (!json.get("access").getAsBoolean()) {
                            System.out.println("Cant connection to room");
                            continue;
                        }

                        new Thread(new Print()).start();

                        gameIsActive = true;

                        while (gameIsActive) {
                            try {
                                control = scanner.nextLine();
                                if (gameIsActive) {
                                    dataTransfer.sendForward(control);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                        }

                        Thread.sleep(1000);

                        break;
                    case "3":
                        System.out.println("Enter name room");
                        String nameRoom = scanner.nextLine();
                        dataTransfer.sendCommand("CreateRoom", nameRoom, "nameRoom");
                        break;
                    case "4":
                            System.out.println(dataTransfer.getRecords());
                        break;
                    default:
                        System.out.println("Error enter data");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public SnakeClientGame(DataTransferGame dataTransfer) {
//        this.dataTransfer = dataTransfer;
    }

    public class Print implements Runnable {

        @Override
        public void run() {
/*            try {
                while (gameIsActive) {
                    frame = dataTransfer.getFrame();
                    print();
                    if (frame.contains("Game over")) {
                        gameIsActive = false;
                    } else {
                        Thread.sleep(Config.threadRestTime - Config.threadRestTime / 2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    private void print() {
/*        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Client");

        System.out.println(frame);*/
    }
}
