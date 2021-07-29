package com.company.server;

import com.company.Config;
import com.company.Vector2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class SnakeServer extends Thread {
    private String frame;
    private String[][] map;
    private LinkedList<Vector2> snake;
    private SnakeClient snakeClient;
    private String moveControl;

    private Boolean leftDirection = false;
    private Boolean rightDirection = false;
    private Boolean upDirection = false;
    private Boolean downDirection = false;

    @Override
    public void run() {
        new Thread(new Move()).start();
        Scanner input = new Scanner(System.in);
        while (true) {
            moveControl = input.nextLine();
            if (moveControl.length() < 1) {
                moveControl = Config.emptiness;
            }
        }
    }

    public SnakeServer(SnakeClient snakeClient) {
        frame = "";
        map = new String[Config.X_SIZE][Config.Y_SIZE];

        int x = 15;
        snake = new LinkedList<>();
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

        createFrame();
        info();


        for (int i = 0; i < 200; i++) {
            foodSpawn();
        }

        moveControl = "a";
    }

    private class Move implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String str = moveControl.charAt(0) + "";

                    Vector2 vector2 = getDirectionsFromKeyboard(
                            str,
                            snake.get(0),
                            leftDirection,
                            rightDirection,
                            upDirection,
                            downDirection
                    );

                    move(vector2, snake);
                    move(snakeClient.getVector2(), snakeClient.getSnake());

                    print();
                    snakeClient.setFrame(frame);

                    Thread.sleep(Config.threadRestTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
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

    private void print() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Server");
        frame = createFrame();
        System.out.println(frame);
    }

    public void printSnake(LinkedList<Vector2> snake, String body) {
        snake.forEach(v -> {
            map[v.getX()][v.getY()] = body;
        });

        Vector2 v = snake.stream().findFirst().get();
        map[v.getX()][v.getY()] = Config.snakeHead;
    }

    public String createFrame() {
        printSnake(snakeClient.getSnake(), Config.BLUE + Config.snakeBody + Config.RESET);
        printSnake(snake, Config.RED + Config.snakeBody + Config.RESET);

        var ref = new Object() {
            String print = "";
        };

        Arrays.stream(map).forEach(f -> {
                Arrays.stream(f).forEach(x -> ref.print = ref.print +  x + Config.emptiness);
                ref.print = ref.print + "\n";
            }
        );

        ref.print += Config.RED_BOLD + "Server snake: " + snake.size() + Config.RESET + "\n";
        ref.print += Config.BLUE_BOLD + "Client snake: " + snakeClient.getSnake().size() + Config.RESET + "\n";

        return ref.print;
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
        String _map = map[snake.get(0).getX()][snake.get(0).getY()];
        if (_map.equals(Config.wall) || _map.equals(Config.snakeBody)) {
            var ref = new Object() {
                Integer f = 15;
            };
            Arrays.asList("Game over").forEach(c -> {
                ref.f = ref.f + 1;
                map[Math.round(Config.X_SIZE/2)][ref.f] = c;
            });

            print();
            snakeClient.setFrame(frame);
            snakeClient.close();

            System.exit(0);
        }
    }

    public void remove(LinkedList<Vector2> snake) {
        map[snake.get(snake.size() - 1).getX()][snake.get(snake.size() - 1).getY()] = Config.emptiness;
        snake.removeLast();
    }

    public static Vector2 getDirectionsFromKeyboard(String forward, Vector2 headSnake, Boolean lDir, Boolean rDir, Boolean uDir, Boolean dDir) {
        if (forward.equals("w") && !rDir) {
            lDir = true;
            uDir = false;
            dDir = false;
        } else if (forward.equals("s") && !lDir) {
            rDir = true;
            uDir = false;
            dDir = false;
        } else if (forward.equals("a") && !dDir) {
            uDir = true;
            rDir = false;
            lDir = false;
        } else if (forward.equals("d") && !uDir) {
            dDir = true;
            rDir = false;
            lDir = false;
        }

        Vector2 vector2 = null;
        if (lDir) {
            vector2 = (new Vector2(headSnake.getX() - 1, headSnake.getY()));
        } else if (rDir) {
            vector2 = (new Vector2(headSnake.getX() + 1, headSnake.getY()));
        } else if (uDir) {
            vector2 = (new Vector2(headSnake.getX(), headSnake.getY() - 1));
        } else if (dDir) {
            vector2 = (new Vector2(headSnake.getX(), headSnake.getY() + 1));
        }

        return vector2;
    }
}
