package com.company.server;

import com.company.Vector2;

import java.util.LinkedList;
import java.util.Scanner;

public class SnakeServer extends Snake implements Runnable {
    private String moveController;
    private Thread getInput;

    public SnakeServer(LinkedList<Vector2> snake) {
        super(snake);
        this.moveController = "";
        getInput = new Thread(new GetInput());
    }

    @Override
    public void run() {
        getInput.start();
        synchronized (this) {
            while (true) {
                try {
                    print();
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Server");
        System.out.println(super.getFrame());
    }

    private class GetInput implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    moveController = scanner.nextLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public synchronized void updateFrame(String frame) {
        notify();
        setFrame(frame);
        getDirectionsFromKeyboard(moveController, getSnake().get(0));
    }

    @Override
    public void close() {
        getInput.stop();
    }
}
