package com.company.server;

import com.company.Vector2;

import java.util.LinkedList;

public class SnakeClientRef extends Snake implements Runnable {
    private DataTransfer dataTransfer;
    private String moveController;
    private Thread getInput;

    public SnakeClientRef(LinkedList<Vector2> snake, DataTransfer dataTransfer) {
        super(snake);
        this.dataTransfer = dataTransfer;
        this.moveController = "";
        getInput = new Thread(new GetInput());
    }

    @Override
    public void run() {
        getInput.start();
        synchronized (this) {
            while (!dataTransfer.isClose()) {
                try {
                    dataTransfer.sendFrame(super.getFrame());
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private class GetInput implements Runnable {
        @Override
        public void run() {
            getDirectionsFromKeyboard(moveController, getSnake().get(0));
            while (true) {
                try {
                    moveController = dataTransfer.getForward().charAt(0) + "";
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
        dataTransfer.close();
    }
}
