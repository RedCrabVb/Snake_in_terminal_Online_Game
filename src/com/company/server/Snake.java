package com.company.server;

import com.company.Controller;
import com.company.Vector2;
import com.company.server.menu.Performance;

import java.util.LinkedList;

public abstract class Snake {
    private final LinkedList<Vector2> snake;
    private final String color;
    private final String name;
    private final Performance menu;
    private Vector2 vector2;
    private String frame = "";
    private Controller controller = Controller.up;

    public Snake(LinkedList<Vector2> snake, String color, Performance menu) {
        this.name = menu.getUsername();
        this.color = color;
        this.snake = snake;
        this.menu = menu;
    }

    public Vector2 getVector2() {
        return vector2;
    }

    public LinkedList<Vector2> getSnake() {
        return snake;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getFrame() {
        return frame;
    }

    public void getDirectionsFromKeyboard(String forward, Vector2 headSnake) {
        if (forward.contains(Controller.left.getKey()) && controller.isNotOpposite(Controller.left)) {
            controller = Controller.left;
        } else if (forward.contains(Controller.right.getKey()) && controller.isNotOpposite(Controller.right)) {
            controller = Controller.right;
        } else if (forward.contains(Controller.up.getKey()) && controller.isNotOpposite(Controller.up)) {
            controller = Controller.up;
        } else if (forward.contains(Controller.down.getKey()) && controller.isNotOpposite(Controller.down)) {
            controller = Controller.down;
        }

        switch (controller) {
            case right:
                vector2 = new Vector2(headSnake.getX() + 1, headSnake.getY());
                break;
            case left:
                vector2 = new Vector2(headSnake.getX() - 1, headSnake.getY());
                break;
            case down:
                vector2 = new Vector2(headSnake.getX(), headSnake.getY() + 1);
                break;
            case up:
                vector2 = new Vector2(headSnake.getX(), headSnake.getY() - 1);
                break;
            default:
                break;
        }
    }

    public abstract void updateFrame(String frame);

    public abstract void close();

    public String getColor() {
        return color;
    }

    public abstract Thread getThread();

    public String getName() {
        return name;
    }

    public Performance getMenu() {
        return menu;
    }
}
