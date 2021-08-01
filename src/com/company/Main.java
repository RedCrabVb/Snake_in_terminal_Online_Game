package com.company;

import com.company.client.DataTransferG;
import com.company.client.SnakeClientG;
import com.company.server.Room;
import com.company.server.command.CommandSwitch;
import com.company.server.command.ConnectionToRoom;
import com.company.server.command.GetRoomList;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 or 2: \n 1) Create server \n 2) Connect server");
        String selection = scanner.nextLine();
        if (selection.equals("1")) {
            System.out.println("Enter port for program");
            String port = scanner.nextLine();

            new MenuServer(rooms).start();

            ServerSocket socket = new ServerSocket(Integer.parseInt(port));
            while (true) {
                Socket socketClient = socket.accept();
                new MenuClient(new DataTransfer(socketClient), rooms).start();
            }

            /*rooms.add(new Room("First room"));
            snake.addUser();
            snake.addUser(new DataTransfer(socketClient));
            System.out.println(snake.isReady());
            Thread.sleep(1000);
            snake.start();*/
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

//    public static class MenuClient extends Thread {
//        private DataTransfer dataTransfer;
//        private CommandSwitch commandSwitch;
//
//        private MenuClient(DataTransfer dataTransfer, List<Room> roomList) {
//            this.dataTransfer = dataTransfer;
//            this.commandSwitch = new CommandSwitch();
//            this.commandSwitch.register("GetRoomList", new GetRoomList(roomList, dataTransfer));
//            this.commandSwitch.register("ConnectionToRoom", new ConnectionToRoom(dataTransfer, roomList));
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    commandSwitch.execute(dataTransfer.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//        }
//    }

//    public static class MenuServer extends  Thread {
//        @Override
//        public void run() {
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                System.out.println("Enter menu item");
//                System.out.println("1. Show list rooms");
//                System.out.println("2. Connection to rooms");
//                System.out.println("3. Create rooms");
//                String inputData = scanner.nextLine();
//
//                switch (inputData) {
//                    case "1":
//                        System.out.println(getListRooms());
//                        break;
//                    case "2":
//                        System.out.println("Enter number rooms");
//                        int numberRoom = Integer.parseInt(scanner.nextLine()) - 1;
//                        rooms.get(numberRoom).addUser();
//                        if (rooms.get(numberRoom).isReady()) {
//                            rooms.get(numberRoom).start();
//                        }
//                        break;
//                    case "3":
//                        System.out.print("Enter name rooms: ");
//                        rooms.add(new Room(scanner.nextLine()));
//                        break;
//                    default:
//                        System.out.println("Error enter data");
//                        break;
//                }
//            }
//        }
//    }
}
