package com.company.server.menu;

import com.company.server.SocketAcceptConnection;
import com.company.client.DataTransferGame;
import com.company.client.SnakeClientGame;
import com.company.server.performance.Performance;

import java.io.IOException;
import java.net.Socket;

public class ServerUI implements UI {
    private ConsoleUI consoleUI;
    private Performance performance;

    public ServerUI(ConsoleUI consoleUI, int port) {
        this.consoleUI = consoleUI;
        new Thread(new SocketAcceptConnection(port)).start();
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    @Override
    public void showAllRooms() {
        System.out.println(SocketAcceptConnection.getListRooms());
    }

    @Override
    public synchronized void connectionToRooms(int number) {
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

    public void createServer(int port) {
        new Thread(new SocketAcceptConnection(port)).start();
    }

    public void connectionServer(String ip, int port) throws IOException {
        SnakeClientGame snakeClientGame = new SnakeClientGame(new DataTransferGame(new Socket(ip, port)));
        snakeClientGame.start();
    }

    @Override
    public boolean registration(String login, String password) {
        return SocketAcceptConnection.dataBase.isRealAccount(login, password);
    }
}
