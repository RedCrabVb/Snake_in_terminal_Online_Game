package com.company.server;

import com.company.Config;
import com.company.DataTransfer;
import com.company.Vector2;
import com.company.server.command.CommandSwitch;
import com.company.server.command.GetFrame;
import com.company.server.command.SetDirection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private String frame;
    private String[][] map;
    private SnakeClient snakeClient;
    private SnakeServer snakeServer;
    public static CommandSwitch commandSwitch;

    public Server(DataTransfer dataTransfer) {
        frame = "";
        map = new String[Config.X_SIZE][Config.Y_SIZE];

        int x = 15;
        LinkedList<Vector2> snake = new LinkedList<>(Arrays.asList(
                new Vector2(x, 15),
                new Vector2(x, 16),
                new Vector2(x, 17),
                new Vector2(x, 18)));
        x += 3;
        LinkedList<Vector2> snake2 = new LinkedList<>(Arrays.asList(
                new Vector2(x, 15),
                new Vector2(x, 16),
                new Vector2(x, 17),
                new Vector2(x, 18)));
        snakeClient = new SnakeClient(snake, dataTransfer);
        snakeServer = new SnakeServer(snake2);

        info();
        for (int i = 0; i < 100; i++) {
            foodSpawn();
        }
        frame = createFrame();

        this.commandSwitch = new CommandSwitch();
        commandSwitch.register("GetFrame", new GetFrame(dataTransfer, snakeClient));
        commandSwitch.register("SetDirection", new SetDirection(snakeClient));

        new Thread(snakeClient).start();
        new Thread(snakeServer).start();
        start();
    }

    private void start() {
        while (true) {
            try {
                frame = createFrame();

                snakeServer.updateFrame(frame);
                snakeClient.updateFrame(frame);

                move(snakeServer.getVector2(), snakeServer.getSnake());
                move(snakeClient.getVector2(), snakeClient.getSnake());

                Thread.sleep(Config.threadRestTime);
            } catch (Exception e) {
                e.printStackTrace();
                break;
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

    public void printSnake(LinkedList<Vector2> snake, String body) {
        snake.forEach(v -> map[v.getX()][v.getY()] = body);

        Vector2 v = snake.stream().findFirst().get();
        map[v.getX()][v.getY()] = Config.snakeHead;
    }

    public String createFrame() {
        printSnake(snakeClient.getSnake(), Config.BLUE + Config.snakeBody + Config.RESET);
        printSnake(snakeServer.getSnake(), Config.RED + Config.snakeBody + Config.RESET);

        var ref = new Object() {
            String print = "";
        };

        Arrays.stream(map).forEach(f -> {
                    Arrays.stream(f).forEach(x -> ref.print = ref.print + x + Config.emptiness);
                    ref.print = ref.print + "\n";
                }
        );

        ref.print += Config.RED_BOLD + "Server snake: " + snakeServer.getSnake().size() + Config.RESET + "\n";
        ref.print += Config.BLUE_BOLD + "Client snake: " + snakeClient.getSnake().size() + Config.RESET + "\n";

        return ref.print;
    }

    public void foodSpawn() {
        boolean spawnFood = false;
        int x = 0, y = 0;
        while (!spawnFood) {
            x = (int) (Math.round(Math.random() * Config.X_SIZE));
            y = (int) (Math.round(Math.random() * Config.Y_SIZE));
            spawnFood = true;

            if (x == 0 || y == 0 || x >= (Config.X_SIZE - 1) || y >= (Config.Y_SIZE - 1)) {
                spawnFood = false;
            }

            List<Vector2> snakeList = new LinkedList();
            snakeList.addAll(snakeClient.getSnake());
            snakeList.addAll(snakeServer.getSnake());
            for (var v : snakeList) {
                if (x == v.getX() && y == v.getY()) {
                    spawnFood = false;
                }
            }
        }

        map[x][y] = Config.food;
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

    private void gameOver() {
        var ref = new Object() {
            Integer f = 15;
        };
        Arrays.asList("Game over").forEach(c -> {
            ref.f = ref.f + 1;
            map[Math.round(Config.X_SIZE / 2)][ref.f] = c;
        });

        frame = createFrame();
        snakeClient.updateFrame(frame);
        snakeServer.updateFrame(frame);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        snakeClient.close();
        snakeServer.close();

        System.exit(0);
    }

    private void hitTheObject(LinkedList<Vector2> snake) {
        String _map = map[snake.get(0).getX()][snake.get(0).getY()];
        if (_map.contains(Config.wall) || _map.contains(Config.snakeBody)) {
            gameOver();
        }
    }

    public void remove(LinkedList<Vector2> snake) {
        map[snake.get(snake.size() - 1).getX()][snake.get(snake.size() - 1).getY()] = Config.emptiness;
        snake.removeLast();
    }

}
