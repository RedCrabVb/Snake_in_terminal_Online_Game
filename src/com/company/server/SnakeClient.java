package com.company.server;

import com.company.DataTransfer;
import com.company.Vector2;
import com.company.server.Server;

import java.util.LinkedList;

public class SnakeClient extends Snake implements Runnable {
    private DataTransfer dataTransfer;
    private String moveController;

    public SnakeClient(LinkedList<Vector2> snake, DataTransfer dataTransfer) {
        super(snake);
        this.dataTransfer = dataTransfer;
        this.moveController = "";
    }

    @Override
    public void run() {
        while (true) {
            try {
                Server.commandSwitch.execute(dataTransfer.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void setMoveController(String moveController) {
        this.moveController = moveController;
    }

    @Override
    public synchronized void updateFrame(String frame) {
        setFrame(frame);
        getDirectionsFromKeyboard(moveController, getSnake().get(0));
    }

    @Override
    public void close() {
        dataTransfer.close();
    }
}
