package com.company.client;

import com.company.Config;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Scanner;

public class SnakeClientG extends Thread {
    private volatile String frame = "";
    private volatile DataTransferG dataTransfer;
    private volatile String control = "a";
    private volatile boolean gameIsActive = false;

    private Thread print;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter menu item");
            System.out.println("1. Show list rooms");
            System.out.println("2. Connection to rooms");
            String inputData = scanner.nextLine();
            switch (inputData) {
                case "1":
                    try {
                        System.out.println("get list room");
                        System.out.println(dataTransfer.getListRoom());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    System.out.println("Enter number room");
                    int numberRoom = Integer.valueOf(scanner.nextLine());
                    try {
                        dataTransfer.connectionToRoom(numberRoom);
                        JsonObject json = dataTransfer.getMessage();
                        if (!json.get("access").getAsBoolean()) {
                            System.out.println("Cant connection to room");
                            continue;
                        }
                        print = new Thread(new Print());
                        print.start();

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

                        dataTransfer.sendMessage("");

                        Thread.sleep(4000);

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Error enter data");
                    break;
            }
        }
    }

    public SnakeClientG(DataTransferG dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    private class Print implements Runnable {

        @Override
        public void run() {
            try {
                while (gameIsActive) {
                    frame = dataTransfer.getFrame();
                    print();
                    if (frame.contains("Game over")) {
                        gameIsActive = false;
                    } else {
                        Thread.sleep(Config.threadRestTime);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Client");

        System.out.println(frame);
    }
}
