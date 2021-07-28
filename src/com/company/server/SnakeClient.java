package com.company.server;

import com.company.Config;
import com.company.Vector2;

import java.util.LinkedList;

public class SnakeClient extends Thread {
    private String map;
    private Vector2 vector2;
    private int threadRestTime = 2000;
    private LinkedList<Vector2> snake;
    private DataTransfer dataTransfer;
    private String control = "w";

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    @Override
    public void run() {
        new Thread(new Move()).start();
        new Thread(new GetMove()).start();
        int error = 1;
        while (error < 10) {
            try {
                dataTransfer.sendMap(map);
                Thread.sleep(Config.threadRestTime);
            } catch (Exception e) {
                e.printStackTrace();
                error++;
            }
        }
    }

    public SnakeClient(DataTransfer dataTransfer, LinkedList<Vector2> snake) {
        this.dataTransfer = dataTransfer;
        this.snake = snake;
    }

    private class GetMove implements Runnable {

        @Override
        public void run() {
            int error = 1;
            while (error < 10) {
                try {
                    control = dataTransfer.getForward();
                } catch (Exception e) {
                    e.printStackTrace();
                    error++;
                }
            }
        }
    }

    private class Move implements Runnable {

        @Override
        public void run() {
            while (true) {
                control = control.charAt(0) + "";

                if (control.equals("w") && !rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if (control.equals("s") && !leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if (control.equals("a") && !downDirection) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                } else if (control.equals("d") && !upDirection) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                if (leftDirection) {
                    vector2 = (new Vector2(snake.get(0).getX() - 1, snake.get(0).getY()));
                } else if (rightDirection) {
                    vector2 = (new Vector2(snake.get(0).getX() + 1, snake.get(0).getY()));
                } else if (upDirection) {
                    vector2 = (new Vector2(snake.get(0).getX(), snake.get(0).getY() - 1));
                } else if (downDirection) {
                    vector2 = (new Vector2(snake.get(0).getX(), snake.get(0).getY() + 1));
                }
            }
        }
    }


    public Vector2 getVector2() {
        return vector2;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public LinkedList<Vector2> getSnake() {
        return snake;
    }

    public void setSnake(LinkedList<Vector2> snake) {
        this.snake = snake;
    }
}
