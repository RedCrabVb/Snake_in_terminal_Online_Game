package com.company.server.menu;

import com.company.SocketAcceptConnection;
import com.company.client.DataTransferGame;
import com.company.client.SnakeClientGame;

import java.io.IOException;
import java.net.Socket;

public class ClientUI implements UI {
    private ConsoleUI consoleUI;

    public ClientUI(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void showAllRooms() {
        System.out.println(SocketAcceptConnection.getListRooms());
    }

    @Override
    public synchronized void connectionToRooms(int number, Performance performance) {
        try {
            SocketAcceptConnection.addUserToRoom(number, performance);
            consoleUI.wait();
        } catch (IndexOutOfBoundsException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRoom(String nameRoom) {
        SocketAcceptConnection.addRooms(nameRoom);
    }

    @Override
    public void showRecorde() {
        System.out.println(SocketAcceptConnection.dataBase.getRecorde());
    }

    @Override
    public void createServer(int port) {
        new Thread(new SocketAcceptConnection(port)).start();
    }

    @Override
    public void connectionServer(String ip, int port) throws IOException {
        SnakeClientGame snakeClientGame = new SnakeClientGame(new DataTransferGame(new Socket(ip, port)));
        snakeClientGame.start();
    }
}
