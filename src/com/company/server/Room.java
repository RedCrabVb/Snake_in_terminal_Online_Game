package com.company.server;

import com.company.Config;
import com.company.DataTransfer;
import com.company.Main;
import com.company.Vector2;
import com.company.server.menu.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Room extends Thread {
    private String frame;
    private final String[][] map;
    private final List<Snake> snakeList;
    private final List<Menu> menus;
    private final List<Thread> snakeThreadList;

    private int startPosition = 15;
    private final String nameRoom;

    public Room(String nameRoom) {
        this.nameRoom = nameRoom;
        frame = "";
        map = new String[Config.X_SIZE][Config.Y_SIZE];
        snakeThreadList = new ArrayList<>();
        snakeList = new ArrayList<>();
        menus = new ArrayList<>();
    }

    public void addUser(DataTransfer dataTransfer, Menu menu) {
        LinkedList<Vector2> snake = new LinkedList<>(Arrays.asList(
                new Vector2(startPosition, 15),
                new Vector2(startPosition, 16),
                new Vector2(startPosition, 17),
                new Vector2(startPosition, 18)));
        startPosition += 3;
        SnakeClient snakeClient = new SnakeClient(snake, dataTransfer);
        snakeList.add(snakeClient);
        snakeThreadList.add(new Thread(snakeClient));
        this.menus.add(menu);
        if (isReady()) {
            this.start();
        }
    }

    public void addUser(Menu menu) {
        LinkedList<Vector2> snake = new LinkedList<>(Arrays.asList(
                new Vector2(startPosition, 15),
                new Vector2(startPosition, 16),
                new Vector2(startPosition, 17),
                new Vector2(startPosition, 18)));
        startPosition += 3;
        SnakeServer server = new SnakeServer(snake);
        snakeList.add(server);
        snakeThreadList.add(new Thread(server));
        this.menus.add(menu);
        if (isReady()) {
            this.start();
        }
    }

    @Override
    public void run() {
        createWall();
        for (int i = 0; i < 100; i++) {
            foodSpawn();
        }

        snakeThreadList.forEach(Thread::start);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(Config.threadRestTime);

                frame = createFrame();

                snakeList.forEach(snake -> {
                    snake.updateFrame(frame);
                    move(snake.getVector2(), snake.getSnake());
                });

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void createWall() {
        for (int i = 0; i < Config.X_SIZE; i++) {
            for (int j = 0; j < Config.Y_SIZE; j++) {
                if (i == 0 || i == Config.X_SIZE - 1 || j == 0 || j == Config.Y_SIZE - 1) {
                    map[i][j] = Config.wall;
                } else {
                    map[i][j] = Config.emptiness;
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
        snakeList.forEach(s -> printSnake(s.getSnake(), s.getColor() + Config.snakeBody + Config.RESET));

        var ref = new Object() {
            String print = "";
        };

        Arrays.stream(map).forEach(f -> {
                    Arrays.stream(f).forEach(x -> ref.print = ref.print + x + Config.emptiness);
                    ref.print = ref.print + "\n";
                }
        );

        snakeList.forEach(s -> ref.print += s.getColor() + " " + s.getName() + " " + s.getSnake().size() + Config.RESET + "\n");

        return ref.print;
    }

    public void foodSpawn() {
        boolean spawnFood = false;
        int x = 0, y = 0;
        while (!spawnFood) {
            x = (int) (Math.round(Math.random() * Config.X_SIZE));
            y = (int) (Math.round(Math.random() * Config.Y_SIZE));

            spawnFood = !(x == 0 || y == 0 || x >= (Config.X_SIZE - 1) || y >= (Config.Y_SIZE - 1));

            List<Vector2> snakeList = new LinkedList<>();
            snakeList.addAll(this.snakeList.get(0).getSnake());
            snakeList.addAll(this.snakeList.get(1).getSnake());
            for (var v : snakeList) {
                if (x == v.getX() && y == v.getY()) {
                    spawnFood = false;
                    break;
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
        snakeList.forEach(s -> s.updateFrame(frame));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        snakeList.forEach(Snake::close);
        snakeThreadList.forEach(t -> t.interrupt());
        for (var m : menus) {
            synchronized (m) {
                m.notifyAll();
            }
        }
        Main.removeRoom(this);
        this.interrupt();
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

    @Override
    public String toString() {
        return String.format("%d/2 %s", snakeList.size(), nameRoom);
    }

    public boolean isReady() {
        return snakeList.size() == 2;
    }
}
