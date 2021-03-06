package com.company.server.menu;

import com.company.ParseConfig;
import com.company.server.SocketAcceptConnection;
import com.company.client.DataTransferGame;
import com.company.server.performance.Performance;
import com.company.server.performance.PerformanceServer;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private Performance performance;
    private UI ui;

    String startConfig = "Enter 1 or 2: \n 1) Create server \n 2) Connect server";
    String enterPortForProgram = "Enter port for program: ";
    String ipServer = "Enter ip server: ";
    String portServer = "Enter port server: ";
    String usersHello = "\n\nEnter your username and password, \n" +
            "if a user with this name does not exist, \n" +
            "then it will be created, \n" +
            "if it exists and the password is wrong, \n" +
            "then there will be an error during registration\n";
    String getUser = "Username: ";
    String getPassword = "Password: ";
    String menu = "Enter menu item\n" +
            "1. Show list rooms\n" +
            "2. Connection to rooms\n" +
            "3. Create rooms\n" +
            "4. Users recorde\n";
    String enterNumberRoom = "Enter number rooms: ";
    String enterNameRoom = "Enter name rooms: ";
    String falseEnterData = "Error enter data: ";


    public synchronized void run(String[] args) throws IOException, SQLException {
        scanner = new Scanner(System.in);

        System.out.println(startConfig);
        int input = Integer.valueOf(scanner.nextLine());
        if (input == 1) {
            SocketAcceptConnection.dataBase = ParseConfig.createDataBase(args);
            System.out.print(enterPortForProgram);
            int port = Integer.valueOf(scanner.nextLine());

            ui = new ServerUI(this, port);
//            ui.createServer(port);
        } else if (input == 2) {
            String ip, port;
            System.out.println(ipServer);
            ip = "127.0.0.1";//scanner.nextLine();
            System.out.print(portServer);
            port = scanner.nextLine();

            ui = new ClientUI(ip, Integer.parseInt(port));
//            ui.connectionServer(ip, Integer.parseInt(port));
        } else {
            throw new IOException(falseEnterData);
        }

        System.out.println(usersHello);

        String login, password;
        System.out.print(getUser);
        login = scanner.nextLine();
        System.out.print(getPassword);
        password = scanner.nextLine();


        if (ui instanceof ServerUI) {
            this.performance = new PerformanceServer(login, this);
            ((ServerUI) ui).setPerformance(performance);
        }

        if (!ui.registration(login, password)) {
            System.out.println(falseEnterData);
            System.exit(0);
        }

        while (true) {
            System.out.println(menu);

            try {
                String inputData = scanner.nextLine();

                switch (inputData) {
                    case "1":
                        ui.showAllRooms();
                        break;
                    case "2":
                        System.out.print(enterNumberRoom);
                        int numberRoom = Integer.parseInt(scanner.nextLine()) - 1;
                        ui.connectionToRooms(numberRoom);
                        break;
                    case "3":
                        System.out.print(enterNameRoom);
                        ui.createRoom(scanner.nextLine());
                        break;
                    case "4":
                        ui.showRecorde();
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
