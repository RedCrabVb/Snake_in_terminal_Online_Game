package com.company.server;

import com.company.Config;
import com.company.Vector2;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class SnakeServer extends Thread {
    private String mapString = "";
    private String[][] map = new String[Config.X_SIZE][Config.Y_SIZE];;
    private LinkedList<Vector2> snake = new LinkedList<>();
    private SnakeClient snakeClient;
    private String moveControl = "a";

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    @Override
    public void run() {
        new Move().start();
        Scanner input = new Scanner(System.in);
        while (true) {
            moveControl = input.nextLine();
        }
    }

    public SnakeServer(Socket socket, SnakeClient snakeClient) throws IOException {
        int x = 15;
        snake.add(new Vector2(x, 15));
        snake.add(new Vector2(x, 16));
        snake.add(new Vector2(x, 17));
        snake.add(new Vector2(x, 18));
        x += 3;
        LinkedList<Vector2> snake2 = new LinkedList<>();
        snake2.add(new Vector2(x, 15));
        snake2.add(new Vector2(x, 16));
        snake2.add(new Vector2(x, 17));
        snake2.add(new Vector2(x, 18));
        this.snakeClient = snakeClient;
        this.snakeClient.setSnake(snake2);

        print();
        info();


        for (int i = 0; i < 4; i++) {
            foodSpawn();
        }
    }

    private class Move extends Thread {

        @Override
        public void run() {
            while (true) {
                String str = moveControl.charAt(0) + "";

                if (str.equals("w") && !rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if (str.equals("s") && !leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if (str.equals("a") && !downDirection) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                } else if (str.equals("d") && !upDirection) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                if (leftDirection) {
                    move(new Vector2(snake.get(0).getX() - 1, snake.get(0).getY()), snake);
                } else if (rightDirection) {
                    move(new Vector2(snake.get(0).getX() + 1, snake.get(0).getY()), snake);
                } else if (upDirection) {
                    move(new Vector2(snake.get(0).getX(), snake.get(0).getY() - 1), snake);
                } else if(downDirection){
                    move(new Vector2(snake.get(0).getX(), snake.get(0).getY() + 1), snake);
                }

                move(snakeClient.getVector2(), snakeClient.getSnake());
                print();
                snakeClient.setMap(mapString);
                System.out.println(mapString);

                try {
                    Thread.sleep(Config.threadRestTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void info() {
        for (int i = 0; i < Config.X_SIZE; i++) {
            for (int j = 0; j < Config.Y_SIZE; j++) {
                if (i == 0) {
                    map[i][j] = Config.wall;
                }
                if (i == Config.X_SIZE - 1) {
                    map[i][j] = Config.wall;
                } else {
                    if (j == 0 || j == Config.Y_SIZE - 1) {
                        map[i][j] = Config.wall;
                    } else if (i != 0) {
                        map[i][j] = Config.emptiness;
                    }
                }
            }
        }
    }

    public void printSnake(LinkedList<Vector2> snake) {
        snake.forEach(v -> {
            map[v.getX()][v.getY()] = Config.snakeBody;
        });

        Vector2 v = snake.stream().findFirst().get();
        map[v.getX()][v.getY()] = Config.snakeHead;
    }

    public void print() {
        printSnake(snakeClient.getSnake());
        printSnake(snake);

        var ref = new Object() {
            String print = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
        };

        Arrays.stream(map).forEach(f -> {
                Arrays.stream(f).forEach(x -> ref.print = ref.print +  x + Config.emptiness);
                ref.print = ref.print + "\n";
            }
        );

        mapString = ref.print;
    }

    public void foodSpawn() {
        int x = (int) (Math.round(Math.random() * Config.X_SIZE));
        int y = (int) (Math.round(Math.random() * Config.Y_SIZE));
        if (x == 0 || y == 0 || x == Config.X_SIZE || y == Config.Y_SIZE) {
            foodSpawn();
        } else {
            snake.forEach(v -> {
                if (x == v.getX() && y == v.getY()) {
                    foodSpawn();
                } else {
                    map[x][y] = Config.food;
                }
            });
        }
    }

    private void move(Vector2 move, LinkedList<Vector2> snake) {
        snake.addFirst(move);
        hitTheObject(snake);
        if (map[snake.get(0).getX()][snake.get(0).getY()].equals(Config.food)) {
            snake.addFirst(move);
            foodSpawn();
        }
        remove(snake);
    }

    private void hitTheObject(LinkedList<Vector2> snake) {
        if (map[snake.get(0).getX()][snake.get(0).getY()].equals(Config.wall)
         || map[snake.get(0).getX()][snake.get(0).getY()].equals(Config.snakeBody)) {
            var ref = new Object() {
                Integer f = 15;
            };
            Arrays.asList("Game over").forEach(c -> {
                ref.f = ref.f + 1;
                map[Math.round(Config.X_SIZE/2)][ref.f] = c;
            });
            print();
            System.out.println(mapString);
            System.exit(0);
        }
    }

    public void remove(LinkedList<Vector2> snake) {
        map[snake.get(snake.size() - 1).getX()][snake.get(snake.size() - 1).getY()] = Config.emptiness;
        snake.removeLast();
    }
}
