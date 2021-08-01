package com.company.server;

import com.company.Config;
import com.company.DataTransfer;
import com.company.Vector2;
import com.company.server.command.CommandSwitch;
import com.company.server.command.GetFrame;
import com.company.server.command.SetDirection;

import java.io.EOFException;
import java.util.LinkedList;

public class SnakeClient extends Snake implements Runnable {
    private DataTransfer dataTransfer;
    private String moveController;
    private CommandSwitch commandSwitch;

    public SnakeClient(LinkedList<Vector2> snake, DataTransfer dataTransfer) {
        super(snake, Config.RED, "client");
        this.dataTransfer = dataTransfer;
        this.moveController = "";


        this.commandSwitch = new CommandSwitch();
        commandSwitch.register("GetFrame", new GetFrame(dataTransfer, this));
        commandSwitch.register("SetDirection", new SetDirection(this));
    }

    @Override
    public void run() {
        while (true) {
            try {
                commandSwitch.execute(dataTransfer.getMessage());
            } catch (EOFException e) {
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
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
    }
}
