package com.company.client;

import com.company.Config;

import java.util.Scanner;

public class SnakeClientG extends Thread {
    private String frame = "";
    private DataTransferG dataTransfer;
    private String control = "a";

    @Override
    public void run() {
        new Thread(new Move()).start();
        new Thread(new Print()).start();
        Scanner scanner = new Scanner(System.in);
        while (!dataTransfer.isClose()) {
            try {
                control = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
                dataTransfer.close();
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
            while (!dataTransfer.isClose()) {
                try {
                    frame = dataTransfer.getFrame();
                    print();
                } catch (Exception e) {
                    e.printStackTrace();
                    dataTransfer.close();
                    break;
                }
            }
        }
    }

    private class Move implements Runnable {

        @Override
        public void run() {
            while (!dataTransfer.isClose()) {
                try {
                    move(control);
                    Thread.sleep(Config.threadRestTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    dataTransfer.close();
                    break;
                }
            }
        }
    }

    private void move(String forward) throws Exception {
        dataTransfer.sendForward(forward);
    }

    private void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Client");

        System.out.println(frame);
    }
}
