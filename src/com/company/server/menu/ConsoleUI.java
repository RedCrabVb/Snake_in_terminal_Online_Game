package com.company.server.menu;

import com.company.ParseConfig;
import com.company.SocketAcceptConnection;
import com.company.client.DataTransferGame;
import com.company.client.SnakeClientGame;
import com.company.server.Room;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UI {
    private Scanner scanner;
    private PerformanceServer performanceServer;

    String startConfig = "Enter 1 or 2: \n 1) Create server \n 2) Connect server";
    String enterPortForProgram = "Enter port for program";
    String ipServer = "Enter ip server";
    String portServer = "Enter port server";
    String usersHello = "Enter your username and password, \n" +
            "if a user with this name does not exist, \n" +
            "then it will be created, \n" +
            "if it exists and the password is wrong, \n" +
            "then there will be an error during registration\n";
    String getUser = "Username";
    String getPassword = "Password";
    String menu = "Enter menu item\n" +
            "1. Show list rooms\n" +
            "2. Connection to rooms\n" +
            "3. Create rooms\n" +
            "4. Users recorde\n";
    String enterNumberRoom = "Enter number rooms";
    String enterNameRoom = "Enter name rooms";
    String falseEnterData = "Error enter data";

    @Override
    public void showAllRooms() {
        System.out.println(SocketAcceptConnection.getListRooms());
    }

    @Override
    public void connectionToRooms(int number) {
        try {
            SocketAcceptConnection.addUserToRoom(number, performanceServer);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createRoom(String nameRoom) {
//        rooms.add(new Room(scanner.nextLine()));
        SocketAcceptConnection.addRooms(nameRoom);
    }

    @Override
    public void showRecorde() {
        System.out.println(SocketAcceptConnection.dataBase.getRecorde());
    }

    @Override
    public void createServer(int port, List<Room> rooms) {
//        new Thread(new PerformanceServer("")).start();
        new Thread(new SocketAcceptConnection(port)).start();
    }

    @Override
    public void connectionServer(String ip, int port) throws IOException {
        SnakeClientGame snakeClientGame = new SnakeClientGame(new DataTransferGame(new Socket(ip, port)));
        snakeClientGame.start();
    }

    public void run(String[] args, List<Room> rooms) throws IOException, SQLException {
        scanner = new Scanner(System.in);

        System.out.println(startConfig);
        int input = scanner.nextInt();
        if (input == 1) {
            SocketAcceptConnection.dataBase = ParseConfig.createDataBase(args);
            System.out.println("Enter port for program");
            int port = scanner.nextInt();
            createServer(port, rooms);
        } else if (input == 2) {
            String ip, port;
            System.out.println(ipServer);
            ip = scanner.nextLine();
            System.out.println(enterPortForProgram);
            port = scanner.nextLine();
            connectionServer(ip, Integer.parseInt(port));
        } else {
            throw new IOException(falseEnterData);
        }

        String login, password;
        System.out.println(getUser);
        login = scanner.nextLine();
        System.out.println(getPassword);
        password = scanner.nextLine();

        this.performanceServer = new PerformanceServer(login);

        if (!SocketAcceptConnection.dataBase.isRealAccount(login, password)) {
            System.out.println(falseEnterData);
            System.exit(0);
        }

        while (true) {
            System.out.println(menu);

            try {
                String inputData = scanner.nextLine();

                switch (inputData) {
                    case "1":
                        showAllRooms();
                        break;
                    case "2":
                        System.out.println(enterNumberRoom);
                        int numberRoom = Integer.parseInt(scanner.nextLine()) - 1;
                        connectionToRooms(numberRoom);
                        break;
                    case "3":
                        System.out.print(enterNameRoom);
                        createRoom(scanner.nextLine());
                        break;
                    case "4":
                        showRecorde();
                        break;
                    default:
                        System.out.println(falseEnterData);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
