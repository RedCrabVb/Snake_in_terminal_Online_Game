package com.company.server.menu;

import com.company.Config;
import com.company.server.SocketAcceptConnection;
import com.company.client.DataTransferGame;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientUI implements UI {
    private final DataTransferGame dataTransfer;
    private volatile String frame = "";
    private volatile String control = "a";
    private volatile boolean gameIsActive = false;

    public ClientUI(String ip, int port) throws IOException {
        this.dataTransfer = new DataTransferGame(new Socket(ip, port));
    }

    @Override
    public void showAllRooms() {
        try {
            System.out.println(dataTransfer.getListRoom());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void connectionToRooms(int number) {
        try {
            dataTransfer.connectionToRoom(number);

            JsonObject json = dataTransfer.getMessage();
            if (!json.get("access").getAsBoolean()) {
                System.out.println("Cant connection to room");
                return;
            }

            new Thread(new Print()).start();

            gameIsActive = true;

            Scanner scanner = new Scanner(System.in);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createRoom(String nameRoom) {
        try {
            dataTransfer.sendCommand("CreateRoom", nameRoom, "nameRoom");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showRecorde() {
        try {
            System.out.println(dataTransfer.getRecords());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    @Override
    public void createServer(int port) {
        new Thread(new SocketAcceptConnection(port)).start();
    }

    @Override
    public void connectionServer(String ip, int port) throws IOException {

    }*/

    @Override
    public boolean registration(String login, String password) {
        try {
            return dataTransfer.registration(login, password);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Client");

        System.out.println(frame);
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
                        Thread.sleep(Config.threadRestTime - Config.threadRestTime / 2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
