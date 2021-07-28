package com.company.client;

import com.company.Config;

import java.util.Scanner;

public class SnakeClientG extends Thread {
    private String map = "";
    private DataTransferG dataTransfer;
    private String control = "a";

    @Override
    public void run() {
        new Thread(new Move()).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                control = scanner.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public SnakeClientG(DataTransferG dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    private class Move implements Runnable {

        @Override
        public void run() {
            int error = 1;
            while (error < 10) {
                try {
                    map = dataTransfer.getMap();
                    print();
                    move(control);
                    Thread.sleep(Config.threadRestTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    error++;
                }
            }
        }
    }

    private void move(String forward) throws Exception {
        dataTransfer.sendForward(forward);
    }

    public void print() {
        System.out.println(map);
    }
}
