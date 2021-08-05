package com.company.server;

import com.company.Config;
import com.company.Vector2;
import com.company.server.performance.Performance;

import java.util.LinkedList;
import java.util.Scanner;

public class SnakeServer extends Snake implements Runnable {
    private String moveController;
    private final Thread getInput;
    private final Thread thread;

    public SnakeServer(LinkedList<Vector2> snake, Performance menu) {
        super(snake, Config.BLUE, menu);
        this.moveController = "";
        getInput = new Thread(new GetInput());
        thread = new Thread(this);
    }

    @Override
    public void run() {
        getInput.start();
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    print();
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
            while (!Thread.currentThread().isInterrupted()) {
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
        getInput.interrupt();
    }

    @Override
    public Thread getThread() {
        return thread;
    }
}
