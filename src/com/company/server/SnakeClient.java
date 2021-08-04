package com.company.server;

import com.company.Config;
import com.company.DataTransfer;
import com.company.Vector2;
import com.company.server.command.CloseRoom;
import com.company.server.command.CommandSwitch;
import com.company.server.command.GetFrame;
import com.company.server.command.SetDirection;
import com.company.server.menu.Menu;

import java.io.EOFException;
import java.util.LinkedList;

public class SnakeClient extends Snake implements Runnable {
    private volatile DataTransfer dataTransfer;
    private String moveController;
    private final CommandSwitch commandSwitch;
    private boolean isCloseRoom = false;
    private final Thread thread;

    public SnakeClient(LinkedList<Vector2> snake, DataTransfer dataTransfer, Menu menu) {
        super(snake, Config.RED, menu);
        this.dataTransfer = dataTransfer;
        this.moveController = "";

        this.commandSwitch = new CommandSwitch();
        this.commandSwitch.register("GetFrame", new GetFrame(dataTransfer, this));
        this.commandSwitch.register("SetDirection", new SetDirection(this));
        this.commandSwitch.register("CloseRoom", new CloseRoom(this));
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !isCloseRoom) {
            try {
                commandSwitch.execute(dataTransfer.getMessage());
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            } catch (EOFException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMoveController(String moveController) {
        this.moveController = moveController;
    }

    @Override
    public void updateFrame(String frame) {
        setFrame(frame);
        getDirectionsFromKeyboard(moveController, getSnake().get(0));
    }

    @Override
    public void close() {
        dataTransfer = null;
        isCloseRoom = true;
    }

    @Override
    public Thread getThread() {
        return thread;
    }
}
