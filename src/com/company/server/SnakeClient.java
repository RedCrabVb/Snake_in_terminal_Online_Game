package com.company.server;

import com.company.Vector2;

import java.util.LinkedList;

public class SnakeClient extends Thread {
    private String frame;
    private Vector2 vector2;
    private LinkedList<Vector2> snake;
    private DataTransfer dataTransfer;
    private String moveControl;

    private Boolean leftDirection = false;
    private Boolean rightDirection = false;
    private Boolean upDirection = false;
    private Boolean downDirection = false;

    @Override
    public synchronized void run() {
        try {
            wait();
            new Thread(new Move()).start();
            while (!dataTransfer.isClose()) {
                try {
                    dataTransfer.sendFrame(frame);
                    wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SnakeClient(DataTransfer dataTransfer, LinkedList<Vector2> snake) {
        this.dataTransfer = dataTransfer;
        this.snake = snake;

        frame = "";
        moveControl = "w";
    }

    private class Move implements Runnable {

        @Override
        public void run() {
            while (!dataTransfer.isClose()) {
                try {
                    moveControl = dataTransfer.getForward().charAt(0) + "";

                    vector2 = SnakeServer.getDirectionsFromKeyboard(
                            moveControl,
                            snake.get(0),
                            leftDirection,
                            rightDirection,
                            upDirection,
                            downDirection
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }


    public Vector2 getVector2() {
        return vector2;
    }

    public synchronized void setFrame(String frame) {
        notify();
        this.frame = frame;
    }

    public LinkedList<Vector2> getSnake() {
        return snake;
    }

    public synchronized void setSnake(LinkedList<Vector2> snake) {
        this.vector2 = snake.get(0);
        this.snake = snake;
        notify();
    }

    public void close() {
        dataTransfer.close();
    }
}
