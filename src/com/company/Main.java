package com.company;

import com.company.client.DataTransferG;
import com.company.client.SnakeClientG;
import com.company.server.DataTransfer;
import com.company.server.SnakeClient;
import com.company.server.SnakeServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 or 2: \n 1) Create server \n 2) Connect server");
        String selection = scanner.nextLine();
        if (selection.equals("1")) {
            System.out.println("Enter port for program");
            String port = scanner.nextLine();

            ServerSocket socket = new ServerSocket(Integer.parseInt(port));
            Socket socketClient = socket.accept();

            SnakeClient snakeClient = new SnakeClient(new DataTransfer(socketClient), new LinkedList<Vector2>());
            SnakeServer snakeMy = new SnakeServer(socketClient, snakeClient);
            snakeMy.start();
            snakeClient.start();
        } else if (selection.equals("2")) {
            System.out.println("Enter ip server");
            String ip = "127.0.0.1";//scanner.nextLine();
            System.out.println("Enter port server");
            String port = scanner.nextLine();

            SnakeClientG snakeClientGame = new SnakeClientG(new DataTransferG(new Socket(ip, Integer.parseInt(port))));
            snakeClientGame.start();
        } else {
            new IOException("Error enter data");
        }
        Thread.sleep(90000);
    }
}
