package com.company.client;

import com.company.Config;

import java.util.Scanner;

public class SnakeClientG extends Thread {
    private String frame = "";
    private DataTransferG dataTransfer;
    private String control = "a";

    private Thread move;
    private Thread print;

    @Override
    public void run() {
//        move = new Thread(new Move());
        print = new Thread(new Print());
//        move.start();
        print.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                control = scanner.nextLine();
                move(control);
            } catch (Exception e) {
                e.printStackTrace();
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
            while (true) {
                try {
                    frame = dataTransfer.getFrame();
                    print();
                    if (frame.contains("Game over")) {
                        shutdown();
                    }
                    Thread.sleep(Config.threadRestTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

//    private class Move implements Runnable {
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    move(control);
//                    Thread.sleep(Config.threadRestTime);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//        }
//    }

    private void move(String forward) throws Exception {
        dataTransfer.sendForward(forward);
    }

    private void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Client");

        System.out.println(frame);
    }

    private void shutdown() {
//        move.stop();
        dataTransfer.close();
        System.exit(0);
    }
}
